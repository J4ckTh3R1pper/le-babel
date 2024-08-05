<template>
<div class="code-box">
    <span class="header">
        {{shownLang}}
        <span class="copy">
            <FontAwesomeIcon :icon="['far', 'clipboard']"></FontAwesomeIcon>
        </span>
    </span>
            <HighlightJs
                v-if="lang"
                :language="shownLang"
                :code="shownCode"
            ></HighlightJs>
            <HighlightJs
                v-else
                autodetect
                :code="shownCode">
            </HighlightJs>
</div>
</template>

<script setup>
  import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
  import 'highlight.js/lib/common'
  import {computed} from "vue";
  const props = defineProps({
      code: String,
      lang: String,
      escaped: Boolean
  })
  const shownCode = computed( () => props.escaped ? props.code : decodeURI(props.code) )
  const shownLang = computed( () => props.escaped ? props.lang : decodeURI(props.lang) )
</script>

<script>
import hljsVuePlugin from "@highlightjs/vue-plugin";
export default {
    name: "MarkdownCode",
    components: {
        HighlightJs: hljsVuePlugin.component
    }
}
</script>

<style lang="scss">
.code-box {
    background: #6a737d !important;
    padding: 4px 4px 4px 4px !important;
    border-radius: 14px !important;
    text-align: start !important;

    .header {
        font-family: "DejaVu Sans Mono", "Cascadia Code", "Courier New", sans-serif;
        display: block;
        margin: 0 4px 4px 4px;
        line-height: 20px;
    }
    pre {
        margin-top: 0;
        code.hljs {
            padding-top: 0;
        }
    }
}
</style>