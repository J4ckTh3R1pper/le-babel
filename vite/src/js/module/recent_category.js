import {compact, filter, isArray, isEmpty, isNumber, remove, take, uniq, uniqBy} from "lodash";
import {defineStore} from "pinia";
import Cookies from "js-cookie";

const cookiesKey = 'RecentCategory'
const useRecentCategoryStore = defineStore(
    'recent_category',
    {
        state: () => ({
            list: getRecent()
        }),
        actions: {
            updateRecent(obj) {
                // console.log(obj)
                this.list.unshift(obj)
                this.list = compact(uniq(this.list))
                if (this.list.size > 8) {
                    this.list = take(this.list, 8)
                }
                writeRecent(this.list)
            }
        }
    }
)

function getRecent() {
    let string = Cookies.get(cookiesKey)
    let obj = JSON.parse(isEmpty(string) ? '[]' : string)
    let arr = isArray(obj) ? compact(obj) : []
    return filter(arr, o => isNumber(o))
}

function writeRecent(list) {
    Cookies.set(cookiesKey, JSON.stringify(list))
}

export default useRecentCategoryStore