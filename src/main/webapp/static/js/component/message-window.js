import axios from 'axios'
import Message from './message.js'

export default {
	expose: ['showUsrText', 'channelId'],
	props: ['channelId', 'channelName'],
	inject: ['members'],
	components: {
		Message
	},
	data() {
		return {
			messages: [],
			timestamp: Date.now(),
			historyIndex: null
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
		async showUsrText(message) {
			let member;
			function setMember(e) { member = e; }
			if (!this.members.has(message.owner)) {
				let response = await axios.post("/get_member", {
					userId: message.owner,
					serverId: this.serverId
				}, { headers: {
						'Content-Type': 'multipart/form-data'
					}
				})
				member = response.data
			}
			else setMember(this.members.get(message.owner))
			let date = this.dateFmt.format(message.date)
			let avatar = member.avatar ? member.avatar : ( "/static/img/default_avatar.png" )
			this.messages.push({
				"ind": message.ind,
				"type": message.type,
				"owner": message.owner,
				"avatar": avatar,
				"date": date,
				"identity": member.identity,
				"nickname": member.nickname ? member.nickname : member.username,
				"msgText": message.text
			})
		},
		//TODO: 根据ind而不是timestamp来检索历史消息
		//TODO: 缓存最后消息的ind用于刷新后检索历史消息
        async getHistory() {
			let history = [];
            console.log(this.timestamp);
			console.log(this.historyIndex);
            let response = await axios.post("/get_last_messages",
                {   channelId: this.channelId,
					timestamp: !this.historyIndex ? this.timestamp : null,
					index: this.historyIndex
                }, {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}
            )
			history = response.data;
			// history.forEach(e => this.showUsrText(e));
			let iter = history.entries();
			let result = iter.next();
			try {
				while (!result.done) {
					console.log(result.value[1])
					await this.showUsrText(result.value[1]);
					result = iter.next();
				}
			} catch (e) { console.log("error: " + e); }
			finally {
				if (result.done) 
					this.historyIndex = history.at(-1).ind;
				else 
					this.historyIndex = result.value[1].ind;
				console.log("historyIndex changed to: " + this.historyIndex);

			}
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
		<div class="get-history" @click="getHistory" ><i class="fa-solid fa-arrow-up">获取历史记录</i></div>
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
