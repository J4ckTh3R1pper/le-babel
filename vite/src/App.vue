<script setup>
import {ref, computed, onMounted} from 'vue'
import translate from 'i18n-jsautotranslate'
import { asyncTimeout } from './js/utils/utils';

window.translate = translate; //方便审核元素用控制台调试
translate.service.use('client.edge'); //翻译通道
translate.whole.enableAll(); //整体翻译
translate.language.setLocal('chinese_simplified');
// translate.selectLanguageTag.show = false;
//页面渲染完毕后触发执行 translate.execute(); 

async function onInitialized() {
  translate.execute();
  await asyncTimeout(500)
  translate.selectLanguageTag.refreshRender();
  //vue的input中的placeholder属性会在nextTick之后延迟渲染，而这个属性是没有别的方式来监听的，所以额外加一个定时器
  translate.execute();
  translate.listener.start();    //开启html页面变化的监控
}
onUpdated(() => {
  translate.execute();
});
</script>

<template>
  <router-view @initialized="onInitialized"/>
</template>

<style>
body {
  font-family: 'Roboto';
}
</style>
