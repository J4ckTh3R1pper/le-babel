export const UserMessage = {
	props: ['ownerId', 'avatar', 'date', 'identity', 'nickname', 'msgText'],
	template:
		`<li :class="this.userId === this.ownerId ? 'myMessage' : 'otherMessage'">
			<img class='avatar' :src='this.avatar' alt='浏览器不支持!'>
			<div class='literalMsg'>
				<div class='timeAndName'>
					<span v-if="this.identity >= 0" :class="'user-title ' + 'group-' + this.identity">
						{{ identity == 2 ? '群主' : identity == 1 ? '管理员' : '' }}
					</span>
					<span class='username'> {{ nickname }} </span>
					<span class='time'> {{ date }} </span>
				</div>
				<div class='msgText'> {{ msgText }} </div>
			</div>
		</li>`
}

export const SystemMessage = {
	template: 
		`<li class='systemMessage'>
			<slot></slot>
		</li>`
}

export default {
	props: ['ownerId', 'avatar', 'date', 'identity', 'nickname', 'msgText', 'msgType'],
	components: {
		UserMessage,
		SystemMessage
	},
	template:
	`<UserMessage
		v-if="this.msgType === 'user'"
		:owner-id="this.ownerId"
		:avatar="this.avatar"
		:date="this.date"
		:identity="this.identity"
		:nickname="this.nickname"
		:msg-text="this.msgText"
	>
	</UserMessage>
	<SystemMessage
		v-else-if="this.type === 'sys'"
	>
		{{ msgText }}
	</SystemMessage>
	`
}

