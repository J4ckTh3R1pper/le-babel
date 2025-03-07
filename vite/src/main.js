import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import '@fortawesome/fontawesome-free/css/all.css'
import ElementPlus from 'element-plus'

const app  = createApp(App).use(ElementPlus);

app.mount('#app')
