<template>
  <el-tooltip placement="top">
    <template #content>{{info}}</template>
    <el-menu-item router="true" :index="url" >
      <el-avatar :src="imgUrl"/>
      <el-text>{{name}}</el-text>
    </el-menu-item>
  </el-tooltip>
</template>

<script setup>
import { computed } from "vue";
import {useRoute, useRouter} from "vue-router";

const route = useRoute()
const router = useRouter()

const {imgUrl, name, categoryId, info} = defineProps({
  // route object
  imgUrl: {
    type: String,
    required: true
  },
  name: {
    type: String,
    required: true
  },
  categoryId: {
    type: Number | String,
    required: true
  },
  info: {
    type: String,
    required: true
  }
})

const url = computed(() => {
  return "/c/" + categoryId
})
</script>
