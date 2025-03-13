<script setup>
import useAppStore from "@/js/module/app.js";
import {Clock} from "@element-plus/icons-vue";
import CategoryLink from "@/components/Sidebar/CategoryLink.vue";
import RecentCategory from "@/components/Sidebar/RecentCategory.vue";

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

</script>

<template>
  <el-aside>
    <div class="sidebar">
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-menu
            background-color="#000000"
            text-color="#FFFFFF"
            active-text-color="#C79457"
            :collapse-transition="true"
            mode="vertical"
        >
          <RecentCategory />
        </el-menu>
      </el-scrollbar>
    </div>
  </el-aside>
</template>

<style scoped lang="scss">

</style>