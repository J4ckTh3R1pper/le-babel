<template>
  <div class="user-membership">
    <el-avatar class="avatar" :src="user.headImgUrl"/>
    <span class="data">
        <span>
          <span v-if="user.role > 1" :class="'role_' + user.role">{{ user.role == 3 ? '版主' : user.role == 2 ? '管理员' : '' }}</span>
          <el-link class="nickname" :href="`/userInfo?id=${userId}`">{{user.nickName}}</el-link>
        </span>
        <el-text v-if="isEmpty(user.title)" class="title">{{user.title}}</el-text><br>
        <span class="exp" v-if="user.role > 0" >
          <el-text class="level">{{'等级' + user.level}}</el-text>
          <el-progress :percentage="user.experience % 100" :format="format"/>
        </span>
      </span>
  </div>
</template>

<script setup>
import useUserCacheStore from '@/js/module/user_cache'
import { isEmpty, isNumber } from 'lodash'
import defaultAvatar from '@/assets/images/default_user_avatar.png'

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

watch(() => categoryId, async () => {
  user.value = await userCacheStore.fetchUserBriefView(categoryId, userId)
})

const format = (percentage) => percentage + '/100' 

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
  flex-grow: 1;
  flex-direction: column;
  & > * {
    padding-bottom: 4px;
  }
  .nickname {
    flex-grow: 0;
    align-self: flex-start;
  }
  .exp {
    display: flex;
    flex-grow: 1;
    .el-progress {
        margin-left: 5px;
        flex-grow: 1;
    }
  }
}
  .role_3, .role_2 {
    border-radius: 4px;
    padding: 4px 3px;
    font-size: 12px;
    margin-right: 4px;
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