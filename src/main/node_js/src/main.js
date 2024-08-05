import { createApp } from 'vue'
import './style.css'
import hljsVuePlugin from "@highlightjs/vue-plugin";
import App from './App.vue'
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {library} from "@fortawesome/fontawesome-svg-core";
import {faClipboard} from "@fortawesome/free-regular-svg-icons";
import MarkdownCode from "./components/MarkdownCode.vue";

library.add(faClipboard)
const app  = createApp(App);
app.use(hljsVuePlugin)
app.component('FontAwesomeIcon', FontAwesomeIcon)
    .component('MarkdownCode', MarkdownCode)
app.mount('#app')
