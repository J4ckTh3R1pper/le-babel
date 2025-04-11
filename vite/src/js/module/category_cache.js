import { defineStore } from 'pinia'
import { getCategoryInfo } from '@/js/api/category'

const useCategoryCacheStore = defineStore(
    'category_cache', {
    state: () => ({
        categoryMap: new Map()
    }),
    actions: {
        async fetchCategory(categoryId) {
            if (!this.categoryMap.has(categoryId))
                await getCategoryInfo(categoryId).then(res => {
                    let category = {
                        name: res.name,
                        createTime: res.createTime * 1000,
                        avatar : res.avatar,
                        info: res.info,
                        rule: res.rule
                    }
                    this.categoryMap.set(categoryId, category)
                })
        }
    },
    getters: {
        getCategory(state) {
            return (categoryId) => {
                return state.categoryMap.get(categoryId)
            }
        }
    }
})

export default useCategoryCacheStore