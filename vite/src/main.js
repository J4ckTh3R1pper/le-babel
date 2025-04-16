import { createApp } from 'vue'
// import './style.css'
import App from './App.vue'
import '@fortawesome/fontawesome-free/css/all.css'
import '@fontsource/roboto'
import '@fontsource/montserrat/700.css'
// import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {createPinia} from "pinia";
import createRouter from './router/index'


const router = createRouter
const app  = createApp(App)
    // .use(ElementPlus)
    .use(createPinia())
    .use(router)
;

app.mount('#app')
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}