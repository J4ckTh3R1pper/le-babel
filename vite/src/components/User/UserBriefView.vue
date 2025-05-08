<script setup>
import {isEmpty} from "@/js/utils/validate.js";
import defaultAvatar from '@/assets/images/default_user_avatar.png'
import useUserCacheStore from "@/js/module/user_cache";
import { storeToRefs } from "pinia";
import { computed } from "vue";
import { useRoute, useRouter } from "vue-router";

const avatar = ref('');
const router = useRouter()

const {categoryId, userId, full} = defineProps({
  categoryId: {
    type: Number,
    required: true
  },
  userId: {
    type: Number,
    required: true
  },
  full: {
    type: Boolean,
    default: false
  }
})

const userCacheStore = useUserCacheStore()

const user = ref(await userCacheStore.fetchUserBriefView(categoryId, userId))

watchEffect(() => {
  avatar.value = isEmpty(user.value.headImgUrl) ? defaultAvatar : user.value.headImgUrl
})

const avatarStyle = computed(() => ({
  'margin-right': full ? "1em" : "0.5em"
}))

</script>

<template>
  <div class="user-brief-view">
    <el-avatar class="avatar" :src="avatar" :size=" full ? 50 : 25" :style="avatarStyle"/>
    <span class="data">
      <span class="top">
        <span v-if="user.role > 1" :class="'role_' + user.role">{{ user.role == 3 ? '版主' : user.role == 2 ? '管理员' : '' }}</span>
        <el-link class="nickname" @click.self.prevent="router.push({name: 'user_profile', params:{id: userId}})">{{user.nickName}}</el-link>
        <span v-if="user.role > 0" >
          <el-divider direction="vertical" border-style="dashed"/>
          <el-text class="level">{{'Lv.' + user.level}}</el-text>
        </span>
        <el-text v-if="isEmpty(user.title)" class="title">{{user.title}}</el-text>
      </span>
      <span v-if="full" class="bottom">
        <span class="location">{{user.location}}</span>
      </span>
    </span>
  </div>
</template>

<style scoped lang="scss">
  .user-brief-view {
    display: flex;
    align-items: center;

    .location {
      font-size: small;
      color: #646464;
    }
  }

  .avatar {
    margin-right: 1em;
  }

  .data {
    display: flex;
    flex-direction: column;
  }
  .role_3 {
    background-color: #FF4500;
    color: #FFFFFF;
  }
  .role_2 {
    background-color: #1E90FF;
    color: #FFFFFF;
  }
  
</style>