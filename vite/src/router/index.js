import { createRouter, createWebHistory } from "vue-router";
import constantRoutes from "./routes.js";
import useRouteHistoryStore from "@/js/module/route_history.js";

const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes
})

router.afterEach((to, from) => {
    let historyStore = useRouteHistoryStore()
    historyStore.update(from)
})

export default router