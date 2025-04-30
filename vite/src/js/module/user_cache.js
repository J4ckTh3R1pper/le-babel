import {defineStore} from "pinia";
import {getUserCache,getMemberCache} from "@/js/api/user.js";
import { isEmpty } from "lodash";

const useUserCacheStore = defineStore(
    'user_cache',
    {
        state: () => ({
            memberMap: new Map(),
            userMap: new Map()
        }),
        actions: {
            async fetchUser(userId, renew) {
                return new Promise((resolve, reject) => {
                    if (renew) return reject()
                    if (this.userMap.has(userId)) {
                        return resolve(this.userMap.get(userId))
                    } else return reject(userId)
                }).catch(async (userId) => {
                    let user;
                    await getUserCache(userId).then(res => {
                        if (isEmpty(res)) throw new Error()
                        user = res
                    }).catch(r => {
                        user = ghostUser
                    }).finally(() => {
                        this.userMap.set(userId, user)
                    })
                    return Promise.resolve(user)
                })
            },
            async fetchMember(categoryId, userId, renew) {
                return new Promise((resolve, reject) => {
                    if (renew) return reject()
                    if (!this.memberMap.has(categoryId)) {
                        this.memberMap.set(categoryId, new Map());
                        return reject()
                    }
                    if (this.memberMap.get(categoryId).has(userId))
                        return resolve(this.memberMap.get(categoryId).get(userId))
                    return reject()
                }).catch(async () => {
                    let member;
                    await getMemberCache(categoryId, userId).then(res => {
                        if (isEmpty(res))
                            throw new Error()
                        member = res
                    }).catch(() => {
                        member = defaultMember
                        // console.log(member)
                    }).finally(() => {
                        this.memberMap.get(categoryId).set(userId, member)
                    })
                    return Promise.resolve(member)
                })
            },
            async fetchUserBriefView(categoryId, userId, renew) {
                return this.fetchUser(userId, renew).then(async (user) => {
                    let member = await this.fetchMember(categoryId, userId, renew)
                    return Promise.resolve({
                        nickName: user.nickName,
                        headImgUrl: user.headImgUrl,
                        location: user.location,
                        role: member.role,
                        level: Math.round(member.experience / 100),
                        title: member.title
                    })
                })
            }
        },
        getters: {
            getUserBriefView(state) {
                return (categoryId, userId) => {
                    const user = state.userMap.get(userId)
                    const member = state.memberMap.get(categoryId, userId)
                }
            }
        }
            
})

const ghostUser = {
    nickName: "ghost",
    headImgUrl: "/assets/images/default_user_avatar.png",
    location: ""
}

const defaultMember = {
    role: 0,
    experience: 0,
    title: null
}

export default useUserCacheStore