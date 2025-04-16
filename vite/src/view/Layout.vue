<script setup>

import Sidebar from "@/components/Sidebar";
import Header from "@/components/Header"
import useAppStore from "@/js/module/app";
import { storeToRefs } from "pinia";
import "element-plus/theme-chalk/display.css"
import RecentPost from "@/components/Post/RecentPost.vue";

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
      <el-main class="main-wrapper">
        <suspense>
          <router-view v-slot="{ Component }">
            <keep-alive :include="['Index', 'PostDetail', 'CategoryIndex']">
              <component :is="Component"></component>
            </keep-alive>
          </router-view>
        </suspense>
      </el-main>
      <el-aside class="side-card hidden-sm-and-down" width="400px">
        <!-- TODO: 放置侧边卡片 -->
         <component :is="RecentPost"></component>
      </el-aside>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">

.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  overflow-y: visible;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
  // @include clearfix;
}

.container {
  height: calc(100% - 50px);
  padding-bottom: 0;
}

.main-wrapper {
  padding-bottom: 0;
  overflow-y: visible;
}
.side-card {
  padding: 20px 0px;
  padding-right: 20px;
}
.header-wrapper {
  display: flex;
  flex-direction: row;
  flex-grow: 1;
  align-items: stretch;
  padding: 0;
}
// @import "@/assets/styles/mixin.scss";
// @import "@/assets/styles/variables.module.scss";
</style>