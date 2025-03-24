<script setup>

import useUserStore from "@/js/module/user.js";
import {getCaptchaRequest, registerRequest} from "@/js/api/login.js";
import {ElMessageBox} from "element-plus";
import {useRoute, useRouter} from "vue-router";
const { proxy } = getCurrentInstance();
const route = useRoute()
const router = useRouter()

const registerForm = ref({
  loginName: "",
  password: "",
  nickName: "",
  confirmPassword: "",
  uuid: "",
  captcha: ""
})

const captchaImgUrl = ref("");
const redirect = ref(undefined);
const loading = ref(false);

watch(route, (newRoute) => {
  redirect.value = newRoute.query && newRoute.query.redirect;
}, { immediate: true });

onMounted(() => {
  getCaptcha()
})

function getCaptcha() {
  getCaptchaRequest().then( res => {
    captchaImgUrl.value = res.img;
    registerForm.value.uuid = res.uuid;
  })
}

const confirmPassword = (rule, value, callback) => {
  if (registerForm.value.password !== value) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const registerRules = {
  loginName: [
    { required: true, trigger: "blur", message: "请输入您的注册邮箱" },
  ],
  password: [
    { required: true, trigger: "blur", message: "请输入您的密码" },
    { min: 8, max: 32, message: "用户密码长度必须介于 8 和 32 之间", trigger: "blur" },
    { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
  ],
  nickName: [
    { required: true, trigger: "blur", message: "请输入您的昵称" }
  ],
  confirmPassword: [
    { required: true, trigger: "blur", message: "请再次输入您的密码" },
    { required: true, validator: confirmPassword, trigger: "blur" }
  ],
  captcha: [{ required: true, trigger: "change", message: "请输入验证码" }]
};


function handleRegister() {
  proxy.$refs.registerRef.validate( valid => {
    if (valid) {
      loading.value = true;
      let data = {
        loginName: registerForm.value.loginName,
        password: registerForm.value.password,
        nickName: registerForm.value.nickName,
        uuid: registerForm.value.uuid,
        captcha: registerForm.value.captcha
      }
      registerRequest(data).then(() => {
        const username = data.loginName;
        ElMessageBox.alert("<span style='color: red;'>" + "恭喜你，您的账号 " + username + " 注册成功！</span>", "系统提示", {
          dangerouslyUseHTMLString: true,
          type: "success",
        }).then(() => {
          router.push("/login");
        }).catch(() => {});

      }).catch(() => {
        getCaptcha()
        loading.value = false
      })
    }
  })
}

</script>

<template>
  <div class="register">
    <el-form ref="registerRef" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">注册</h3>
      <el-form-item prop="loginName">
        <el-input
            v-model="registerForm.loginName"
            type="text"
            size="large"
            auto-complete="off"
            placeholder="注册邮箱"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="nickName">
        <el-input v-model="registerForm.nickName"
                  type="text"
                  size="large"
                  autocomplete="off"
                  placeholder="昵称"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="registerForm.password"
            type="password"
            size="large"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter="handleRegister"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            size="large"
            auto-complete="off"
            placeholder="确认密码"
            @keyup.enter="handleRegister"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="captcha">
        <el-input
            v-model="registerForm.captcha"
            size="large"
            auto-complete="off"
            placeholder="验证码"
            style="width: 63%"
            @keyup.enter="handleRegister"
        >
        </el-input>
        <div class="captcha">
          <img :src="captchaImgUrl" @click="getCaptcha" class="captcha-img"/>
        </div>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            size="large"
            type="primary"
            style="width:100%;"
            @click.prevent="handleRegister"
        >
          <span v-if="!loading">注册</span>
          <span v-else>注册中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.register {
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

.register-form {
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
.register-tip {
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
    padding-left: 12px;}
}
.el-register-footer {
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