import { defineStore } from 'pinia'
import { getCategoryCache, getCategoryInfo } from '@/js/api/category'

const useCategoryCacheStore = defineStore(
    'category_cache', {
    state: () => ({
        categoryMap: new Map()
    }),
    actions: {
        async fetchCategory(categoryId) {
            return new Promise((resolve, reject) => {
                if (this.categoryMap.has(categoryId))
                    return resolve(this.categoryMap.get(categoryId))
                else return reject(categoryId)
            }).catch( async (categoryId) => {
                let category;
                await getCategoryCache(categoryId).then(res => {
                    category = {
                        name: res.name,
                        avatar : res.avatar,
                        info: res.info,
                    }
                    this.categoryMap.set(categoryId, category)
                })
                return Promise.resolve(category);
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