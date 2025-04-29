<script setup>
import {useRoute, useRouter} from "vue-router";
import request from "@/js/utils/request.js";
import useLoginUserStore from "@/js/module/login_user.js";
import {getCaptchaRequest} from "@/js/api/login.js";
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();

const userStore = useLoginUserStore();

const loginForm = ref({
  loginName: "",
  password: "",
  rememberMe: false,
  captcha: '',
  uuid: ''
});

const loginRules = {
  loginName: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  captcha: [{ required: true, trigger: "change", message: "请输入验证码" }]
};

// 验证码开关
const captchaImgUrl = ref("");
// 注册开关
const redirect = ref(undefined);
const loading = ref(false);

watch(route, (newRoute) => {
redirect.value = newRoute.query && newRoute.query.redirect;
}, { immediate: true });

function getCaptcha() {
    getCaptchaRequest().then( res => {
        captchaImgUrl.value = res.img;
        loginForm.value.uuid = res.uuid;
    })
}

onMounted(() => {
  getCaptcha()
})

function handleLogin() {
    proxy.$refs.loginRef.validate( valid => {
        if (valid) {
          loading.value = true;

          userStore.login(loginForm.value).then(async () => {
            const query = route.query;
            const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
              if (cur !== "redirect") {
                acc[cur] = query[cur];
              }
              return acc;
            }, {});
            router.push({ path: redirect.value || "/", query: otherQueryParams });
          }).catch(() => {
            loading.value = false;
            // 重新获取验证码
            getCaptcha();
          });
        }
    })
}

</script>

<template>
  <div class="login">
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">登录</h3>
      <el-form-item prop="loginName">
        <el-input
            v-model="loginForm.loginName"
            type="text"
            size="large"
            auto-complete="off"
            placeholder="登录邮箱"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="loginForm.password"
            type="password"
            size="large"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter="handleLogin"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="captcha">
        <el-input
            v-model="loginForm.captcha"
            size="large"
            auto-complete="off"
            placeholder="验证码"
            style="width: 63%"
            @keyup.enter="handleLogin"
        >
        </el-input>
        <div class="captcha">
          <img :src="captchaImgUrl" @click="getCaptcha" class="captcha-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">保持登录状态</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            size="large"
            type="primary"
            style="width:100%;"
            @click.prevent="handleLogin"
        >
          <span v-if="!loading">登录</span>
          <span v-else>登录中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.captcha {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
    height: 40px;
    padding-left: 12px;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>