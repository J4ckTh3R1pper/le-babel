<script setup>
import 'highlight.js/styles/stackoverflow-dark.css';
import {computed, onBeforeMount, onUnmounted, ref} from 'vue';
import MarkdownIt from "markdown-it";
import MarkdownItHighlightJS from "markdown-it-highlightjs"
import ClipboardJS from "clipboard";
import Token from "markdown-it/lib/token.mjs";
import {asyncTimeout} from "../js/utils.js";

const props = defineProps({
  mdText: String
})

// 初始化markdown-it
let md = new MarkdownIt();
md.use(MarkdownItHighlightJS);

const btn_replacement = '<pre><button class="cpy-btn"><i class="fa-regular fa-clipboard fa-fw"></i><span class="cpy-tooltip">复制代码</span></button>';

//定义默认渲染函数
const proxy = (tokens, idx, options, env, self) => self.renderToken(tokens, idx, options);

// 重写fence型代码块的渲染函数
const defaultFenceRenderer = md.renderer.rules.fence || proxy;
md.renderer.rules.fence = function(tokens, idx, options, env, self) {
  const html = defaultFenceRenderer(tokens, idx, options, env, self);
  return html.replace('<pre>', btn_replacement);
}

// 重写代码块渲染函数
const defaultCodeBlockRenderer = md.renderer.rules.code_block || proxy;
md.renderer.rules.code_block = function(tokens, idx, options, env, self) {
  const html = defaultCodeBlockRenderer(tokens, idx, options, env, self);
  return html.replace('<pre>', btn_replacement);
}

const result = computed(() => {
  return md.render(props.mdText);
})

// 初始化粘贴按钮行为
let cpy_btn = new ClipboardJS('.cpy-btn', {
  target: (e) => {
    return e.nextElementSibling;
  }
});
cpy_btn.on('success', async function(e) {
  e.clearSelection();
  e.trigger.firstElementChild.setAttribute('class', 'fa-solid fa-check fa-fw');
  document.querySelector('.cpy-tooltip').innerHTML = '已复制！'
  await asyncTimeout(3000);
  e.trigger.firstElementChild.setAttribute('class', 'fa-regular fa-clipboard fa-fw')
  document.querySelector('.cpy-tooltip').innerHTML = '复制代码'
})

onBeforeMount(()=> {
})

onUnmounted(() => {
  // 在Vue组件生命周期结束前销毁ClipboardJS对象
  cpy_btn.destroy();
})
</script>

<template>
  <span v-html="result"></span>
</template>

<style lang="scss" scoped>
* {
  text-align: start !important;
}

:deep(pre) {
  padding: 4px 4px 4px 4px !important;
  border-radius: 14px !important;
  margin-top: 0;
  position: relative;

  .header {
    font-family: "DejaVu Sans Mono", "Cascadia Code", "Courier New", sans-serif;
    display: block;
    margin: 0 4px 4px 4px;
    line-height: 20px;
  }
  .fa-check {
    color: #76c490;
  }
  button {
    all: unset;
    font-size: 19px;
    position: absolute;
    right: 0.6em;
    top: 0.6em;
    cursor: pointer;
    padding: 0.3em 0.4em;
    border-radius: 6px;
    color: #FFFFFF60;
    transition: color, background-color;
    transition-duration: 100ms;

    .cpy-tooltip {
      position: absolute;
      right: 115%;
      top: 0;
      visibility: hidden;
      font-size: 15px;
      padding: 0.58em 0.6em;
      border-radius: 6px;
      background-color: #FFFFFF15;
    }

    &:hover {
      color: #FFFFFF;
      background-color: #FFFFFF15;

      .cpy-tooltip {
        visibility: visible;
      }

    }

  }

  code.hljs {
    border-radius: 10px;
    font-size: 14px;
    border: #434343 solid 2px;
  }

}
</style>