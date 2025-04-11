import { createApp } from 'vue'
// import './style.css'
import App from './App.vue'
import '@fortawesome/fontawesome-free/css/all.css'
import '@fontsource/roboto'
import '@fontsource/montserrat/700.css'
// import ElementPlus from 'element-plus'
import {createRouter, createWebHistory} from "vue-router";
import constantRoutes from "@/router/index.js";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {createPinia} from "pinia";


const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes
})
const app  = createApp(App)
    // .use(ElementPlus)
    .use(createPinia())
    .use(router)
;

app.mount('#app')
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}