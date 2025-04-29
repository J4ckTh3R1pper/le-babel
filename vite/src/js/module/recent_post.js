import Cookies from "js-cookie";
import { isArray, isEmpty, compact, isNumber, take, uniq, filter } from "lodash";
import { defineStore } from "pinia";
const cookiesKey = 'recentPost'

const useRecentPostStore = defineStore('recent_post', {
    state: () => ({
        postList: getRecent()
    }),
    actions: {
        updateRecent(obj) {
            this.postList.unshift(obj)
            this.postList = compact(uniq(this.postList))
            if (this.postList.size > 10) {
                this.postList = take(this.postList, 10)
            }
            writeRecent(this.postList)
        }
    }
})

function getRecent() {
    let string = Cookies.get(cookiesKey)
    let obj = JSON.parse(isEmpty(string) ? '[]' : string)
    let arr = isArray(obj) ? compact(obj) : []
    return filter(arr, o => isNumber(o))
}

function writeRecent(list) {
    Cookies.set(cookiesKey, JSON.stringify(list))
}

export default useRecentPostStore