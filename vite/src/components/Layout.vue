<script setup>

import Sidebar from "@/components/Sidebar";
import Header from "@/components/Header"
import useAppStore from "@/js/module/app";
import { storeToRefs } from "pinia";

const appStore = useAppStore()
const {sidebar} = storeToRefs(appStore)

</script>

<template>
  <div class="app-wrapper">
    <el-container>
      <el-header height="50px" class="header-wrapper">
        <Header />
      </el-header>
      <el-container class="container">
        <Sidebar/>
        <el-main>
          <keep-alive>
            <router-view/>
          </keep-alive>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
@import "@/assets/styles/mixin.scss";
@import "@/assets/styles/variables.module.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.el-container {
  height: calc(100vh - 50px);
}

.header-wrapper {
  display: flex;
  flex-direction: row;
  flex-grow: 1;
  align-items: stretch;
  padding: 0;
}
</style>