import axios from 'axios';
import { createApp } from 'vue';
import MainComponent from'./component/main-component.js'

const main = MainComponent;

main.mounted = function() {
    this.url = "ws://" + self.location.host + "/server/" + server + "/" + token;
    this.ws = new WebSocket(this.url);
    this.ws.onmessage = (e) => {
        const dataObj = JSON.parse(e.data);
        console.log(dataObj);
        this.$refs['msgWindow'].showUsrText(dataObj);
    }
    console.log("app successfully mounted")
}

const app = createApp(main)
app.config.globalProperties = {
    dateFmt : new Intl.DateTimeFormat('zh-CN', {dateStyle: "short", timeStyle: "medium"}),
    serverId: server,
    userId: userId,
}

app.mount("#app-chat");
