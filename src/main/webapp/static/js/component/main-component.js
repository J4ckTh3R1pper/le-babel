import MessageWindow from "./message-window.js";

export default {
    components: {
        MessageWindow
    },
    data() {
    return {
        username: username,
        avatar: avatar,
        activeChannel: null,
        channels: [],
        cachedInputs: [],
        cachedInput: "",
        url: null,
        ws: null
    }
},
    computed: {
        userAvatarStyle() {
            return {
                "background-image": "url(\"" + this.avatar + "\")"
            }
        }
    },
    watch: {
        cachedInput: {
            handler(newValue) {
                this.cachedInputs.forEach((e) => {
                    if (e.id === this.activeChannel)
                        e.text = newValue;
                });
            }
        },
        activeChannel: {
            //FIXME: 这个侦听器会在activeChannel更新时同时触发一次cachedInput的侦听器
            handler(newValue) {
                this.cachedInputs.forEach((e) => {
                    if (e.id === newValue)
                        this.cachedInput = e.text;
                });
            },
        }
    },
    methods: {
        logout() {
            if (confirm("确定要登出吗？")) {
                const d = new Date();
                document.cookie = "token=;expires=" + d.toUTCString() + ";path=/;";
                window.location = "/login"
            }
        },
        setActive(id) {
            this.activeChannel = id;
        },
        submitText() {
            const inputBar = this.$refs['inputBar']
            const text = inputBar.value.trim()
            const json = JSON.stringify({
                text: text,
                file:"",
                channel: this.activeChannel,
                server: server
            });
            if (text !== "") {
                // console.log(text);
                this.ws.send(json);
                inputBar.value = "";
            }
            else alert("文本不能为空！");
        },
    },
    template: `
		<div class="sidebar">
            <!--TODO: 获取服务器名-->
			<div class="header">这是服务器名字</div>
            <!--TODO: 获取频道列表-->
			<div class="channel-list">
				<div v-for="channel in channels"
				     class="channel"
				     :key="channel.id"
				     @click="setActive(channel.id)"
				     >
				     {{channel.name}}
				</div>
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
		<div class="chat-area">
		    <div v-for="channel in channels" v-show="activeChannel === channel.id" class="msg-component">
	    	        <MessageWindow
	    	            ref="msgWindows"
	    		        :channel-id="channel.id"
	    		        :channel-name="channel.name"
	    		    >
	    		    </MessageWindow>
		    </div>
		    <div class="footer">
			    <div class="input-bar">
			    <!--TODO:为输入框占位符匹配频道名-->
				    <textarea
				        @keyup.enter.prevent="submitText"
				        ref="inputBar"
				        class="text"
				        id="input-text" 
				        v-model="this.cachedInput"
				        placeholder="给#others 发消息"
				    >
				    </textarea>
			    </div>
		    </div>
		</div>
    `
}
