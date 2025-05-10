<script setup>
import useLoginUserStore from '@/js/module/login_user';
import { isEmpty, isNumber } from 'lodash';
import { storeToRefs } from 'pinia';
import { useRoute, useRouter } from 'vue-router';
import SearchBar from './SearchBar.vue';

defineOptions({
  name: 'Header'
})

const route = useRoute()
const router = useRouter()

const loginUserStore = useLoginUserStore()
const {token, userId, nickName, headImgUrl, location, roles} = storeToRefs(loginUserStore)

watch (route, () => {
  if (!isEmpty(token.value))
    loginUserStore.getInfo()
}, {immediate: true})


function getUserRoute() {
  let route = {};
  if (!isNumber(userId.value))
    route = {name: 'login'}
  else {
    route = {
      name: 'user_profile',
      params: {
        id: userId.value
      }
    }
  }
  router.push(route)
}

async function logOut() {
  await loginUserStore.logOut()
  window.location.reload()
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
    <span class="center">
      <SearchBar/>
    </span>
    <span class="right">
      <div id="translate"></div>
      <el-dropdown class="user" @command="handleCommand">
        <el-avatar class="avatar" :src="headImgUrl"/>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile" @click="getUserRoute">{{ !isNumber(userId) ? '登录' : '个人主页' }}</el-dropdown-item>
            <el-dropdown-item v-if="isNumber(userId)" @click="logOut">登出</el-dropdown-item>
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
    .center {
      display: flex;
      justify-content: center;
      flex-grow: 2;
    }
    .avatar {
      margin-left: 0.5em;
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