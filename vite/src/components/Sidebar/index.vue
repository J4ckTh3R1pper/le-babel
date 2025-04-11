<script setup>
import useAppStore from "@/js/module/app.js";
import {Clock} from "@element-plus/icons-vue";
import CategoryLink from "@/components/Sidebar/CategoryLink.vue";
import RecentCategory from "@/components/Sidebar/RecentCategory.vue";
import { storeToRefs } from "pinia";
import { computed } from "vue";

defineOptions({
  name: 'Sidebar'
})

const sortSelected = ref("")
const descending = ref(false)
const sortTypes = [
  { key: "按访问顺序排序",
    value: "recentVisit"
  },{
    key: "按加入时间排序",
    value: "joinDate"
  }, {
    key: "按等级排序",
    value: "experience"
  }, {
    key: "按热度排序",
    value: "rank"
  }
]

const appStore = useAppStore()
const {sidebar} = storeToRefs(appStore)

const {toggleSidebar} = appStore
const width = computed(() => {
  return sidebar.value.opened ? '224' : '65'
})

</script>

<template>
  <el-aside width="collapse">
    <div class="sidebar">
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-menu
          class="menu"
          text-color="#000000"
          active-text-color="#C79457"
          :collapse="!sidebar.opened"
          :collapse-transition="true"
          mode="vertical"
        >
          <RecentCategory />
        </el-menu>
      </el-scrollbar>
    </div>
    <span class="open-button"
      :style="{
        left: width - 20 + 'px',
        transform: sidebar.opened ? 'rotate(180deg)' : 'none'
      }"
    >
      <el-button
        :class="{opened: sidebar.opened}"
        size="large"
        circle
        icon="ArrowRight"
        @click="toggleSidebar(false)"
      />
    </span>
  </el-aside>
</template>

<style scoped lang="scss">
  // .el-aside {
  //   &:not(--el-menu--collapse) {
  //     width: 224px;
  //   }
  // }

  .sidebar {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    height: 100%;
    border-right: #c2c2c2 solid 1px;
    .menu {
      &:not(.el-menu--collapse) {
        width: 224px;
      }
    }
  }

  .open-button {
    position: absolute;
    transition: all var(--el-transition-duration) var(--el-transition-function-ease-in-out-bezier);
    top: 80px;
    z-index: 99;
    color:black;
  }
</style>