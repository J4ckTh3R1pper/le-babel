<script setup>

import Sidebar from "@/components/Sidebar/index.vue";
import Header from "@/components/Header/index.vue"
</script>

<template>
  <div class="app-wrapper">
    <el-container>
      <Header />
      <el-container>
        <Sidebar/>
        <el-main>
          <router-view></router-view>
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
</style>