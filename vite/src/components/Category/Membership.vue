<template>
  <div class="user-membership">
    <el-avatar class="avatar" :src="avatar"/>
    <span class="data">
      <span class="top">
        <span v-if="user.role > 1" :class="'role_' + user.role">{{ user.role == 3 ? '版主' : user.role == 2 ? '管理员' : '' }}</span>
        <el-link class="nickname" :href="`/userInfo?id=${userId}`">{{user.nickName}}</el-link>
        <el-divider/>
        <el-text v-if="isEmpty(user.title)" class="title">{{user.title}}</el-text>
        <div v-if="user.role > 0" >
          <el-text class="level">{{'等级' + user.level}}</el-text>
          <el-progress :percentage="user.level % 100"/>
        </div>
      </span>
    </span>
  </div>
</template>

<script setup>
import useUserCacheStore from '@/js/module/user_cache'

const {userId, categoryId} = defineProps({
    userId: {
        type: Number,
        required: true
    },
    categoryId: {
        type: Number,
        required: true
    }
})
const userCacheStore = useUserCacheStore()

const user = ref(await userCacheStore.fetchUserBriefView(categoryId, userId))
const avatar = ref('')

watchEffect(() => {
  avatar = isEmpty(user.value.headImgUrl) ? defaultAvatar : '/api' + user.value.headImgUrl
})

</script>

<style lang="scss" scoped>
.user-membership {
  display: flex;
  align-items: center;
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