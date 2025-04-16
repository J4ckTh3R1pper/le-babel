<script setup>
import {Clock} from "@element-plus/icons-vue";
import CategoryLink from "@/components/Sidebar/CategoryLink.vue";
import useRecentCategoryStore from "@/js/module/recent_category.js";

const recentStore = useRecentCategoryStore()

const {list} = storeToRefs(recentStore)

</script>

<template>
  <Suspense>
  <el-sub-menu index="recent_category">
    <!--          最近访问-->
    <template #title>
      <el-icon><Clock /></el-icon>
      <span>最近访问</span>
    </template>
    <el-menu-item-group>
      <CategoryLink v-for="id in list"
                    :key="id"
                    :category-id="id"/>
    </el-menu-item-group>
  </el-sub-menu>
  </Suspense>
</template>

<style scoped lang="scss">

</style>