import { compact, concat, uniq, uniqBy } from 'lodash'
import { defineStore } from 'pinia'

const useRouteHistoryStore = defineStore(
    'route_history', {
        state: () => ({
            historyList: []
        }),
        actions: {
            update(route) {
                let history = [{
                    path: route.path,
                    fullPath: route.fullPath,
                    name: route.name,
                    params: route.params
                }]
                let newList = compact(concat(history, this.historyList))
                newList = uniqBy(newList, 'name')
                this.historyList = newList
                // console.log(this.historyList)
            }
        }
    })
export default useRouteHistoryStore