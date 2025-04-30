import {getInfoRequest, loginRequest, logoutRequest} from "@/js/api/login.js";
import {getToken, setToken, removeToken} from "@/js/utils/auth.js";
import {defineStore} from "pinia";
import defaultAvatar from '@/assets/images/default_user_avatar.png'
import {isEmpty, isHttp} from '@/js/utils/validate.js'

const useLoginUserStore = defineStore(
    'user',
    {
        state: () => ({
            token: getToken(),
            userId: null,
            nickName: null,
            headImgUrl: defaultAvatar,
            location: null,
            roles: null,
        }),
        actions: {
            // 登录
            login(userInfo) {
                const loginName = userInfo.loginName.trim()
                const password = userInfo.password
                const captcha = userInfo.captcha
                const uuid = userInfo.uuid
                const rememberMe = userInfo.rememberMe
                return new Promise((resolve, reject) => {
                    loginRequest(loginName, password, captcha, uuid, rememberMe).then(res => {
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            // 获取用户信息
            getInfo() {
                return new Promise((resolve, reject) => {
                    getInfoRequest().then(res => {
                        let avatar = res.headImgUrl || ""
                        avatar = (isEmpty(avatar)) ? defaultAvatar : '/api' + avatar
                        this.userId = res.id
                        this.nickName = res.nickName
                        this.headImgUrl = avatar
                        this.location = res.location
                        resolve()
                    }).catch(error => this.logOut())
                })
            },
            // 退出系统
            logOut() {
                return new Promise((resolve, reject) => {
                    logoutRequest(this.token).then(() => {
                        this.$reset()
                        removeToken()
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                })
            }
        }
    })

export default useLoginUserStore