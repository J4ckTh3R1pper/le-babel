import axios from 'axios'
import Message from './message.js'
export default {
	expose: ['showUsrText'],
	props: ['channelId', 'serverId'],
	components: {
		Message
	},
	data() {
		return {
			messages: [],
			timestamp: Date.now()
		}
	},
	methods: {
		showUsrText(message) {
			axios.post("/get_username", {
					userId: message.owner,
					serverId: this.serverId
				}, {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}
			).then( (resp) => {
				console.log(resp.data);
				let avatar = resp.data.avatar ? resp.data.avatar : ( self.location.host + "/static/img/default_avatar.png" )
				let date = this.dateFmt.format(message.date)
				this.messages.push({
					"ind": message.index,
					"type": message.type,
					"owner": resp.data.userId,
					"avatar": avatar,
					"date": date,
					"identity": resp.data.identity,
					"nickname": resp.data.nickname,
					"msgText": message.text
				})
			}).catch( (error) => {
				console.log(error);
			})
		},
        submitText() {
            const inputBar = this.$refs['inputBar']
            const text = inputBar.value.trim()
            const json = JSON.stringify({
                text: text,
                file:"",
                channel: channel,
                server: server
            });
            if (text !== "") {
                // console.log(text);
                this.ws.send(json);
                inputBar.value = "";
            }
            else alert("文本不能为空！");
        },
		//TODO: 储存最后消息的时间戳,根据储存时间戳请求历史记录
        getHistory() {
            console.log(this.timestamp);
            axios.post("/get_msg_history",
                {   channel: this.channelId,
                    timestamp: this.timestamp,
                }, {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}
            ).then(function(resp) {
                // console.log(data);
                // console.log("current timestamp is: " + this.timestamp);
                resp.data.forEach((msg)=>{
                    this.showUsrText(msg);
                });

                if (resp.data.at(-1)) {
                    this.timestamp = resp.data.at(-1).date - 1000;
                    // console.log("timestamp changed to: " + this.timestamp);
                }
                else {
                    this.timestamp = this.timestamp - 30 * 60 * 1000;
                }
            }).catch(function(error) {
                this.timestamp = this.timestamp - 30 * 60 * 1000;
                // console.log("error data: " + error + ", timestamp changed to: " + this.timestamp);
            })
        },
		goTop() {
			this.$refs['msgWindow'].scrollTop = 0;
		},
		goBottom() {
			let e = $refs['msgWindow'];
			e.scrollTop = scrollHeight;
		}

	},
	template: `
		<div class="header">#这是频道名</div>
		<div class="get-history" @click="getHistory" ><i class="fa-solid fa-arrow-up"></i></div>
		<ul class="messages" ref="msgWindow">
			<Message
				v-for="msg in messages"
				:key="msg.ind"
				:msg-type="msg.type"
				:owner-id="msg.owner"
				:avatar="msg.avatar"
				:date="msg.date"
				:identity="msg.identity"
				:nickname="msg.nickname"
				:msg-text="msg.msgText"
				>
			</Message>
			<div class="footer">
				<div class="input-bar">
				<!--TODO:为输入框占位符匹配频道名-->
					<textarea @keyup.enter="submitText" ref="inputBar" class="text" id="input-text" placeholder="给#others 发消息"></textarea>
				</div>
			</div>
		</ul>
	`
}
