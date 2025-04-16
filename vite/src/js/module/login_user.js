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
                        const user = res
                        let avatar = user.headImgUrl || ""
                        if (!isHttp(avatar)) {
                            avatar = (isEmpty(avatar)) ? defaultAvatar : '/api' + avatar
                        }
                        this.userId = user.id
                        this.nickName = user.nickName
                        this.headImgUrl = avatar
                        this.location = user.location
                        resolve()
                    }).catch(error => { reject(error)})
                })
            },
            // 退出系统
            logOut() {
                return new Promise((resolve, reject) => {
                    logoutRequest(this.token).then(() => {
                        this.token = ''
                        this.roles = []
                        this.permissions = []
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