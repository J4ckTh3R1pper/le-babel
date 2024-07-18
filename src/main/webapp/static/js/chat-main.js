import axios from 'axios';
import { createApp } from 'vue';
import messageWindow from './component/message-window.js';

const mainComponent = {
    components: {
        messageWindow
    },
    data() {
        return {
            username: username,
            avatar: avatar,
            ws: null,
            url: null,
        }
    },
    computed: {
        userAvatarStyle() {
            return {
                "background-image": "url(\"" + this.avatar + "\")"
            }
        }
    },
    methods: {
        logout() {
            if (confirm("确定要登出吗？")) {
                const d = new Date(0);
                document.cookie = "token=;expires=" + d.toUTCString() + ";path=/;";
                window.location = "/login"
            }
        },
    },
    mounted() {
        console.log("app successfully mounted")
        const url = "ws://" + self.location.host + "/server/" + server + "/" + token;
        this.ws = new WebSocket(url);
        this.ws.onmessage = (e) => {
            const dataObj = JSON.parse(e.data);
            console.log(dataObj);
            this.$refs['msgWindow'].showUsrText(dataObj);
        }
    },
    template: `
		<div class="sidebar">
            <!--TODO: 获取服务器名-->
			<div class="header">这是服务器名字</div>
            <!--TODO: 获取频道列表-->
			<div class="channel-list">
				<div class="channel" id="cid-2">这是频道名</div>
			</div>
			<div class="user-console">
				<div class="user-card">
					<div class="avatar" :style="userAvatarStyle"></div>
					<div class="user-name">
						<div class="nickname" v-text="username"></div>
                   <!-- <div class="user-id"></div> -->
					</div>
				</div>
				<button id="logout" @click="logout">登出</button>
			</div>
		</div>
        <!--TODO: 根据频道列表挂载多个聊天窗组件-->
		<div class="chat-area">
			<messageWindow channel-id="2" server-id="1" ref="msgWindow">
			</messageWindow>
		</div>
    `
}



const app = createApp(mainComponent)
app.config.globalProperties = {
    dateFmt : new Intl.DateTimeFormat('zh-CN', {dateStyle: "short", timeStyle: "medium"}),
    serverId: server,
    userId: userId
}

app.mount("#app-chat");

function getContextPath() {
    return self.location.host;
}	
