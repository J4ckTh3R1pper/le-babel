import {defineStore} from "pinia";
import {getUserBriefView, getUserCache} from "@/js/api/user.js";

const useUserCacheStore = defineStore(
    'user_cache',
    {
        state: () => ({
            memberMap: new Map(),
            userMap: new Map()
        }),
        actions: {
            async getUser(userId) {
                let user;
                if ( !this.userMap.has(userId) ) {
                    this.userMap.set(userId, this.addUser(userId))
                    return this.addUser(userId)
                }
            },
            async addUser(userId) {
                let user = null
                this.getUserCache(userId).then(res => {
                    user = res.data
                })
                return user
            },
        }
})

const ghostUser = {
    nickName: "ghost",
    headImgUrl: "/assets/images/default_user_avatar.png",
    location: ""
}

export default useUserCacheStore