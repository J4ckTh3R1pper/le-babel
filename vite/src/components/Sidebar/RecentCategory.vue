<script setup>
import {Clock} from "@element-plus/icons-vue";
import CategoryLink from "@/components/Sidebar/CategoryLink.vue";
import useRecentCategoryStore from "@/js/module/recent_category.js";

const recent = useRecentCategoryStore()

</script>

<template>
  <el-sub-menu>
    <!--          最近访问-->
    <template #title>
      <el-icon><Clock /></el-icon>
      <span>最近访问</span>
    </template>
    <el-menu-item-group>
      <CategoryLink v-for="item in recent.list"
                     :category-id="item.id"
                     :name="item.name"
                     :info="item.info"
                     :img-url="item.avatar"/>
    </el-menu-item-group>
  </el-sub-menu>
</template>

<style scoped lang="scss">

</style>