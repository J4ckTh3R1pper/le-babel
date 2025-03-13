import {defineStore} from "pinia";

const useAppStore = defineStore(
    'app',
    {
        state : () => ({
            sidebar: {
                opened : true,
                noAnim: false,
                hidden: false
            }
        }),
        actions : {
            toggleSidebar(noAnim) {
                this.noAnim = noAnim
                this.sidebar.opened = !this.sidebar.opened
            },
            closeSidebar(noAnim) {
                this.noAnim = noAnim
                this.sidebar.opened = false
            },
            setSidebarHidden(value) {
                this.sidebar.hidden = value
            }
        }
    }
)

export default useAppStore