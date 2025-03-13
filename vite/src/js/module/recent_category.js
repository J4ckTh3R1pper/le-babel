import {isArray, take, uniqBy} from "lodash";
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
                this.list.unshift(obj)
                this.list = uniqBy(this.list, 'id')
                if (this.list.size > 10) {
                    this.list = take(this.list, 10)
                }
                writeRecent(this.list)
            }
        }
    }
)

function getRecent() {
    let list = Cookies.get(cookiesKey)
    return isArray(list) ? list : []
}

function writeRecent(list) {
    Cookies.set(cookiesKey, list)
}

export default useRecentCategoryStore