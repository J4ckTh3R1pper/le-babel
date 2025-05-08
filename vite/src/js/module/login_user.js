import {getInfoRequest, loginRequest, logoutRequest} from "@/js/api/login.js";
import {getToken, setToken, removeToken} from "@/js/utils/auth.js";
import {defineStore} from "pinia";
import defaultAvatar from '@/assets/images/default_user_avatar.png'
import {isEmpty, isHttp} from '@/js/utils/validate.js'
import { isNumber } from "lodash";

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
            async login(userInfo) {
                const loginName = userInfo.loginName.trim()
                const password = userInfo.password
                const captcha = userInfo.captcha
                const uuid = userInfo.uuid
                const rememberMe = userInfo.rememberMe
                return loginRequest(loginName, password, captcha, uuid, rememberMe)
            },
            // 获取用户信息
            async getInfo() {
                getInfoRequest().then(res => {
                    let avatar = res.headImgUrl || ""
                    avatar = (isEmpty(avatar)) ? defaultAvatar : avatar
                    this.userId = res.id
                    this.nickName = res.nickName
                    this.headImgUrl = avatar
                    this.location = res.location
                })
            },
            // 退出系统
            async logOut() {
                return new Promise((resolve, reject) => {
                    logoutRequest(this.token).then(() => {
                        removeToken()
                        this.$reset()
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                })
            }
        },
        getters: {
            isLoggedIn: (state) => isNumber(state.userId)
        }
    })

export default useLoginUserStore