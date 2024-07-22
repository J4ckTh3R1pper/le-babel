import axios from 'axios'
import Message from './message.js'
export default {
	expose: ['showUsrText', 'channelId'],
	props: ['channelId', 'channelName'],
	components: {
		Message
	},
	data() {
		return {
			messages: [],
			timestamp: Date.now()
		}
	},
	computed: {
		sortedMessages() {
			return this.messages.toSorted(function(a, b) {
				return a.ind - b.ind;
			})
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
				let avatar = resp.data.avatar ? resp.data.avatar : ( "/static/img/default_avatar.png" )
				let date = this.dateFmt.format(message.date)
				this.messages.push({
					"ind": message.ind,
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
		//TODO: 根据ind而不是timestamp来检索历史消息
		//TODO: 缓存最后消息的ind用于刷新后检索历史消息
        getHistory() {
            console.log(this.timestamp);
            axios.post("/get_last_messages",
                {   channelId: this.channelId,
                    timestamp: this.timestamp,
                }, {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}
            ).then(resp => {
                // console.log(data);
                // console.log("current timestamp is: " + this.timestamp);
                resp.data.forEach( msg => this.showUsrText(msg) );

                if (resp.data.at(-1)) {
                    this.timestamp = resp.data.at(-1).date - 1000;
                    console.log("timestamp changed to: " + this.timestamp);
                }
            }).catch(error => {
                console.log("error data: " + error + ", timestamp changed to: " + this.timestamp);
            })
        },
		goTop() {
			this.$refs['msgWindow'].scrollTop = 0;
		},
		goBottom() {
			let e = this.$refs['msgWindow'];
			e.scrollTop = e.scrollHeight;
		}

	},
	template: `
		<div class="header">{{channelName}}</div>
		<div class="get-history" @click="getHistory" ><i class="fa-solid fa-arrow-up"></i></div>
		<ul class="messages" ref="msgWindow">
			<Message
				v-for="msg in sortedMessages"
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
		</ul>
	`
}
