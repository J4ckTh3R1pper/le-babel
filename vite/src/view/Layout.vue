<script setup>

import Sidebar from "@/components/Sidebar";
import Header from "@/components/Header"
import useAppStore from "@/js/module/app";
import { storeToRefs } from "pinia";
import "element-plus/theme-chalk/display.css"
import RecentPost from "@/components/Post/RecentPost.vue";
import { useRoute } from "vue-router";
import CategoryInfo from "@/components/Category/CategoryInfo.vue";
import { computed } from "vue";

const appStore = useAppStore()
const {sidebar} = storeToRefs(appStore)
const route = useRoute()

watch(route, () => {
  loading.value = true
})

const sideCardComponent = computed(() => {
  if (route.name == 'category_index' || route.name == 'post_detail') {
    return CategoryInfo
  }

  if (route.name == 'index') {
    return RecentPost
  }
})

const loading = ref(true)

</script>

<template>
  <el-container class="app-wrapper">
    <el-header height="50px" class="header-wrapper">
      <Header />
    </el-header>
    <el-container class="container">
      <Sidebar/>
      <suspense>
        <el-main class="main-wrapper" v-loading="loading">
          <router-view v-slot="{ Component }">
            <transition name="fade">
              <keep-alive :include="['Index', 'PostDetail', 'CategoryIndex']">
                <component :is="Component" @finish-load="loading = false" :key="route.path"></component>
              </keep-alive>
            </transition>
          </router-view>
        </el-main>
      </suspense>
      <suspense>
        <el-aside class="side-card hidden-sm-and-down" width="400px">
          <keep-alive :include="['RecentPost']">
            <component :is="sideCardComponent"></component>
          </keep-alive>
        </el-aside>
      </suspense>
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