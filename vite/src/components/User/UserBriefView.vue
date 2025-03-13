<script setup>
import {isEmpty} from "@/js/utils/validate.js";
import defaultAvatar from '@/assets/img/default_user_avatar.png'

const {
  id, nickName, headImgUrl, role, level, location, title
} = defineProps({
  id: {
    type: Number,
    required: true
  },
  nickName: {
    type: String,
    required: true
  },
  headImgUrl: {
    type: String,
    required: true
  },
  role: {
    type: Number,
    required: true
  },
  level: {
    type: Number,
    required: true
  },
  location: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: false
  }

})

let avatar = ref('');

watchEffect(() => {
  avatar = isEmpty(headImgUrl) ? defaultAvatar : '/api' + headImgUrl
})
</script>

<template>
<div class="user-brief-view">
  <el-avatar class="avatar" :src="headImgUrl"/>
  <el-link class="nickname" :href="`/userInfo?id=${id}`">{{nickName}}</el-link>
  <span v-if="role > 1" :class="'role_' + role">{{ role == 3 ? '版主' : role == 2 ? '管理员' : '' }}</span>
  <span v-if="isEmpty(title)" class="title">{{title}}</span>
  <span v-if="role > 0" class="level">{{'Lv.' + level}}</span>
  <span class="location">{{location}}</span>
</div>
</template>

<style scoped lang="scss">
  .role_3 {
    background-color: #FF4500;
    color: #FFFFFF;
  }
  .role_2 {
    background-color: #1E90FF;
    color: #FFFFFF;
  }
</style>