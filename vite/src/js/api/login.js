import request from "@/js/utils/request.js";
import useLoginUserStore from "../module/login_user";

export function loginRequest(loginName, password, captcha, uuid, rememberMe) {
    const data = {
        loginName: loginName,
        password: password,
        captcha: captcha,
        uuid: uuid,
        rememberMe: rememberMe
    }
    return request.postForm('/api/login', data)
}

// 注册方法
export function registerRequest(data) {
    return request({
        url: '/api/no_auth/user/register',
        headers: {
            isToken: false
        },
        method: 'post',
        data: data
    })
}

// 获取用户详细信息
export async function getInfoRequest() {
    return request({
        url: '/api/auth/user/authorize',
        method: 'get'
    })
}

// 退出方法
export function logoutRequest() {
    return Promise.resolve()
}

// 获取验证码
export function getCaptchaRequest() {
    return request({
        url: '/api/no_auth/user/captcha',
        headers: {
            isToken: false
        },
        method: 'get',
        timeout: 20000
    })
}
