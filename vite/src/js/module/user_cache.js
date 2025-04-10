import {defineStore} from "pinia";
import {getUserCache,getMemberCache} from "@/js/api/user.js";

const useUserCacheStore = defineStore(
    'user_cache',
    {
        state: () => ({
            memberMap: new Map(),
            userMap: new Map()
        }),
        actions: {
            async fetchUser(userId) {
                let user;
                if (!this.userMap.has(userId)) {
                    await getUserCache(userId).then(res => {
                        user = res
                    }).catch(r => {
                        user = ghostUser
                    })
                    this.userMap.set(userId, user)
                }
            },
            async fetchMember(categoryId, userId) {
                let member;
                if (!this.memberMap.has(categoryId))
                    this.memberMap.set(categoryId, new Map());
                if (!this.memberMap.get(categoryId).has(userId)) {
                    await getMemberCache(categoryId, userId).then(res => {
                        if (res == null)
                            reject()
                        member = res
                    }).catch(r => {
                        member = defaultMember
                    }).finally(() => {
                        this.memberMap.get(categoryId).set(userId, member)
                    })
                }
            },
        },
        getters: {
            getUserBriefView(state) {
                return (categoryId, userId) => {
                    const user = state.userMap.get(userId)
                    const member = state.memberMap.get(categoryId, userId)
                    return {
                        nickName: user.nickName,
                        headImgUrl: user.headImgUrl,
                        location: user.location,
                        role: member.role,
                        level: member.experience / 100,
                        title: member.title
                    }
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