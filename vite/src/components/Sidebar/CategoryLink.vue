<template>
  <el-tooltip placement="top" :disabled="isEmpty(data.info)">
    <template #content>{{data.info}}</template>
    <el-menu-item @click="onClicked" :index="'category_link:${categoryId}'">
      <el-avatar :src="data.avatar" class="avatar"/>
      <el-text>{{data.name}}</el-text>
    </el-menu-item>
  </el-tooltip>
</template>

<script setup>
import useCategoryCacheStore from "@/js/module/category_cache";
import { isEmpty } from "lodash";
import { storeToRefs } from "pinia";
import { computed } from "vue";
import {useRoute, useRouter} from "vue-router";

const router = useRouter()

const store = useCategoryCacheStore()

const {categoryId} = defineProps({
  categoryId: {
    type: Number,
    required: true
  },
})

function onClicked() {
  router.push({
    name: 'category_index',
    params: {
      id: categoryId
    }
})
}

const data = await store.fetchCategory(categoryId)
</script>

<style scoped lang="scss">
.avatar {
  margin-right: 8px;
}
</style>
