<script setup>
import {isEmpty} from "@/js/utils/validate.js";
import defaultAvatar from '@/assets/images/default_user_avatar.png'
import useUserCacheStore from "@/js/module/user_cache";
import { storeToRefs } from "pinia";
import { computed } from "vue";

let avatar = ref('');

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

const user = ref({})
await userCacheStore.fetchUser(userId)
await userCacheStore.fetchMember(categoryId, userId)
const {getUserBriefView} = storeToRefs(userCacheStore)
user.value = getUserBriefView.value(categoryId, userId)

watchEffect(() => {
  avatar = isEmpty(user.headImgUrl) ? defaultAvatar : '/api' + user.headImgUrl
})

const avatarStyle = computed(() => ({
  'margin-right': full ? "1em" : "0.5em"
}))

</script>

<template>
  <Suspense>
  <div class="user-brief-view">
    <el-avatar class="avatar" :src="avatar" :size=" full ? 50 : 25" :style="avatarStyle"/>
    <span class="data">
      <span class="top">
        <span v-if="user.role > 1" :class="'role_' + user.role">{{ user.role == '3' ? '版主' : user.role == '2' ? '管理员' : '' }}</span>
        <el-link class="nickname" :href="`/userInfo?id=${userId}`">{{user.nickName}}</el-link>
        <span v-if="user.role > 0" class="level">{{'Lv.' + user.level}}</span>
        <span v-if="isEmpty(user.title)" class="title">{{user.title}}</span>
      </span>
      <span v-if="full" class="bottom">
        <span class="location">{{user.location}}</span>
      </span>
    </span>
  </div>
  </Suspense>
</template>

<style scoped lang="scss">
  .user-brief-view {
    display: flex;
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