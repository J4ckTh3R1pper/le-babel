<script setup>
import useLoginUserStore from '@/js/module/login_user';
import { isEmpty } from '@/js/utils/validate';
import { storeToRefs } from 'pinia';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute()
const router = useRouter()

const loginUserStore = useLoginUserStore()
const {token, id, nickName, headImgUrl, location, roles} = storeToRefs(loginUserStore)

if (!isEmpty(token))
  loginUserStore.getInfo()

function getUserRoute() {
  let url;
  if (id.value == null || id.value == undefined)
    url = "/login"
  else url = "/userInfo/" + id.value
  router.push(url)
}
</script>

<template>
    <div class="header">
      <span class="left"></span>
      <span class="placeholder"></span>
      <span class="right">
        <span class="user">
          <el-avatar class="avatar" :src="headImgUrl"/>
          <el-link @click="getUserRoute"
          >{{ isEmpty(nickName) ? '未登录' : nickName }}</el-link>
        </span>
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