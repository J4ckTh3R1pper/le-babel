<script setup>
import {isEmpty} from "@/js/utils/validate.js";
import defaultAvatar from '@/assets/images/default_user_avatar.png'
import useUserCacheStore from "@/js/module/user_cache";

let avatar = ref('');

const {categoryId, userId} = defineProps({
  categoryId: {
    type: Number,
    required: true
  },
  userId: {
    type: Number,
    required: true
  }
})

const userCacheStore = useUserCacheStore()
const getUserBriefView = userCacheStore.getUserBriefView

const user = getUserBriefView(categoryId, userId)

watchEffect(() => {
  avatar = isEmpty(user.headImgUrl) ? defaultAvatar : '/api' + user.headImgUrl
})
</script>

<template>
<div class="user-brief-view">
  <el-avatar class="avatar" :src="avatar"/>
  <el-link class="nickname" :href="`/userInfo?id=${userId}`">{{user.nickName}}</el-link>
  <span v-if="user.role > 1" :class="'role_' + user.role">{{ user.role == '3' ? '版主' : user.role == '2' ? '管理员' : '' }}</span>
  <span v-if="isEmpty(user.title)" class="title">{{user.title}}</span>
  <span v-if="user.role > 0" class="level">{{'Lv.' + user.level}}</span>
  <span class="location">{{user.location}}</span>
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