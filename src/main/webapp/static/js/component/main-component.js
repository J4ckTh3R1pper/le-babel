import MessageWindow from "./message-window.js";
import { computed } from 'vue'

export default {
    components: {
        MessageWindow
    },
    data() {
		return {
            serverInfo: {
            },
            isMounted: false,
            inputHeight: "42px",
            permission: 0,
			username: "",
			avatar: "",
			activeChannel: null,
			channels: new Map(),
			cachedInputs: new Map(),
			members: new Map(),
			cachedInput: "",
			url: null,
			ws: null
		}
	},
	provide() {
		return {
            serverId: computed( () => this.serverInfo['id'] ),
			members: computed( () => this.members ),
            permission: computed( () => this.permission )
		}
	},
    computed: {
        userAvatarStyle() {
            return {
                "background-image": "url(\"" + (this.avatar ? this.avatar : "/static/img/default_avatar.png") + "\")"
            }
        },
        inputBoxStyle() {
            return {
                "height" : this.inputHeight
            }
        }
    },
    watch: {
        cachedInput: {
            handler(newValue) {
                if ( this.cachedInputs.has(this.activeChannel) )
					this.cachedInputs.set(this.activeChannel, newValue);
			}
        },
        activeChannel: {
            //FIXME: 这个侦听器会在activeChannel更新时同时触发一次cachedInput的侦听器
            handler(newValue) {
                if ( this.cachedInputs.has(newValue) ) {
					this.cachedInput = this.cachedInputs.get(newValue)
				}
			},
        },
    },
    methods: {
        logout() {
            if (confirm("确定要登出吗？")) {
                const d = new Date();
                document.cookie = "token=;expires=" + d.toUTCString() + ";path=/;";
                window.location = "/login"
            }
        },
        updateInputHeight() {
            this.inputHeight = this.$refs['inputBar'].scrollHeight + "px";
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
                server: server['id']
            });
            if (text !== "") {
                // console.log(text);
                this.ws.send(json);
                this.cachedInput = "";
                inputBar.value = "";
            }
            else alert("文本不能为空！");
        },
        goToUrl(url) {
            window.location.href = url
        }
    },
    template: `
		<div class="sidebar">
            <!--TODO: 获取服务器名-->
			<div class="header" @click="goToUrl('/manage_server?serverId=' + serverInfo.id )">
			    <img v-if="serverInfo.avatar" :src="serverInfo.avatar" class="avatar">
			    {{serverInfo.name}}
			</div>
            <!--TODO: 获取频道列表-->
			<div class="channel-list">
			<img v-if="serverInfo.banner" :src="serverInfo.banner">
				<div v-for="[key, value] in channels"
				     :class="'channel ' + (key == this.activeChannel ? 'active' : '')"
				     :key="key"
				     @click="setActive(key)"
				     >
				     {{value}}
				</div>
			</div>
			<div class="user-console">
				<div class="user-card" @click="goToUrl('/edit_user')">
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
		    <div class="header">{{channels.get(activeChannel)}}</div>
		    <div v-for="[key, value] in channels" v-show="activeChannel == key" class="msg-component">
	    	        <MessageWindow
	    	            ref="msgWindows"
	    		        :channel-id="key"
	    		        :channel-name="value"
	    		    >
	    		    </MessageWindow>
		    </div>
		    <div class="footer">
			    <div class="input-bar" :style="inputBoxStyle">
				    <textarea
				        @keyup.enter.prevent="submitText"
				        ref="inputBar"
				        class="text"
				        id="input-text" 
				        v-model="cachedInput"
				        :placeholder="'给 #' + channels.get(activeChannel) + ' 发消息'"
				        :style="inputBoxStyle"
				        @change="updateInputHeight"
				    >
				    </textarea>
			    </div>
		    </div>
		</div>
    `
}
