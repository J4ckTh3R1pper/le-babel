import {defineStore} from "pinia";
import {getUserBriefView} from "@/js/api/user.js";

const useUserCacheStore = defineStore(
    'user_cache',
    {
        state: () => ({
            cacheMap: new Map(),
        }),
        actions: {
            async getUser(categoryId, userId) {
                if ( !this.cacheMap.has(categoryId) ) {
                    this.cacheMap.set(categoryId, new Map())
                    return this.addUser(categoryId, userId)
                }
                const category = this.cacheMap.get(categoryId)
                if ( category.has(userId) )
                    return category.get(userId)
                else return this.addUser(categoryId, userId)
            },
            async addUser(categoryId, userId) {
                let user = null
                getUserBriefView(categoryId, userId).then(res => {
                    user = res.data
                })
                this.cacheMap.get(categoryId).set(userId, user)
                return user
            },
        }
})

export default useUserCacheStore