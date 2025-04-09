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
            id: null,
            nickName: null,
            headImgUrl: null,
            location: null,
            roles: null,
            permissions: null
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
                        setToken(res.token)
                        this.token = res.token
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
                        const user = res.user
                        let avatar = user.headImgUrl || ""
                        if (!isHttp(avatar)) {
                            avatar = (isEmpty(avatar)) ? defaultAvatar : '/api' + avatar
                        }
                        if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
                            this.roles = res.roles
                            this.permissions = res.permissions
                        } else {
                            this.roles = ['ROLE_DEFAULT']
                        }
                        this.id = user.id
                        this.name = user.nickName
                        this.headImgUrl = avatar
                        this.location = user.location
                        resolve(res)
                    }).catch(error => {
                        reject(error)
                    })
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