<template>
  <el-tooltip placement="top">
    <template #content>{{info}}</template>
    <el-menu-item router="true" :index="id" >
      <el-avatar :src="imgUrl"/>
      <el-text>{{name}}</el-text>
    </el-menu-item>
  </el-tooltip>
</template>

<script setup>
import {useRoute, useRouter} from "vue-router";

const route = useRoute()
const router = useRouter()

const props = defineProps({
  // route object
  imgUrl: {
    type: String,
    required: true
  },
  name: {
    type: String,
    required: true
  },
  id: {
    type: Number | String,
    required: true
  },
  info: {
    type: String,
    required: true
  }
})

</script>
