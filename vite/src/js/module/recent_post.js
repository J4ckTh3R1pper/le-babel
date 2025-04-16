import Cookies from "js-cookie";
import { isArray, isEmpty, compact, isNumber, take, uniq, filter } from "lodash";
import { defineStore } from "pinia";
const cookiesKey = 'recentPost'

const useRecentPostStore = defineStore('recent_post', {
    state: () => ({
        list: getRecent()
    }),
    actions: {
        updateRecent(obj) {
            this.list.unshift(obj)
            this.list = compact(uniq(this.list))
            if (this.list.size > 10) {
                this.list = take(this.list, 10)
            }
            writeRecent(this.list)
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