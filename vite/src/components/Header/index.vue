<script setup>
import useLoginUserStore from '@/js/module/login_user';
import { isEmpty, isNumber } from 'lodash';
import { storeToRefs } from 'pinia';
import { useRoute, useRouter } from 'vue-router';

defineOptions({
  name: 'Header'
})

const route = useRoute()
const router = useRouter()

const loginUserStore = useLoginUserStore()
const {token, userId, nickName, headImgUrl, location, roles} = storeToRefs(loginUserStore)

if (!isEmpty(token))
  loginUserStore.getInfo()

function getUserRoute() {
  let url;
  if (isNumber(userId.value))
    url = "/login"
  else url = "/userInfo/" + userId.value
  router.push(url)
}

function logoClicked() {
  router.push({name: 'index'})
}

function handleCommand(command) {

}

</script>

<template>
  <div class="header">
    <span class="left">
      <span class="logo" @click="logoClicked">
        <img src="/icon.png" width="32px" height="32px">
        <span>Le Babel</span>
      </span>
    </span>
    <span class="placeholder"></span>
    <span class="right">
      <el-dropdown class="user" @command="handleCommand">
        <el-avatar class="avatar" :src="headImgUrl"/>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">{{ !isNumber(userId) ? '登录' : '个人主页' }}</el-dropdown-item>
            <el-dropdown-item v-if="isNumber(userId)" command="logOut">登出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </span>
  </div>
</template>

<style scoped lang="scss">
  .header {
    padding: 0 2em;
    display: flex;
    flex-direction: row;
    flex-grow: 1;
    justify-content: space-between;
    background-color: rgb(42, 191, 152);
    .placeholder {
      display: flex;
      flex-grow: 2;
    }
    .avatar {
      margin-right: 0.5em;
      border: 1px rgba(0, 0, 0, 0.32) solid
    }
    .logo {
      display: flex;
      cursor: pointer;
      align-items: center;
      font-family: 'Montserrat';
      font-weight: 800;
      color: #02302c;
      img {
        margin-right: 4px;
      }
    }
    .left {
      display: flex;
      flex-grow: 0;
      align-items: center;
    }
    .right {
      display: flex;
      flex-grow: 0;
      align-items: center;
      .user {
        display: flex;
        flex-grow: 0;
        align-items: center;
      }
    }
  }
</style>