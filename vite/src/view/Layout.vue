<script setup>

import Sidebar from "@/components/Sidebar";
import Header from "@/components/Header"
import useAppStore from "@/js/module/app";
import { storeToRefs } from "pinia";
import "element-plus/theme-chalk/display.css"

const appStore = useAppStore()
const {sidebar} = storeToRefs(appStore)

</script>

<template>
  <el-container class="app-wrapper">
    <el-header height="50px" class="header-wrapper">
      <Header />
    </el-header>
    <el-container class="container">
      <Sidebar/>
      <el-main>
        <suspense>
          <router-view v-slot="{ Component }">
            <keep-alive :include="['Index', 'PostDetails', 'CategoryIndex']">
              <component :is="Component"></component>
            </keep-alive>
          </router-view>
        </suspense>
      </el-main>
      <el-aside class="hidden-sm-and-down">
        <!-- TODO: 放置侧边卡片 -->
      </el-aside>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">
@import "@/assets/styles/mixin.scss";
@import "@/assets/styles/variables.module.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
  overflow-y: hidden;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.container {
  height: 100%;
}

.el-main {
  padding-bottom: 0;
  overflow: hidden;
}

.header-wrapper {
  display: flex;
  flex-direction: row;
  flex-grow: 1;
  align-items: stretch;
  padding: 0;
}
</style>