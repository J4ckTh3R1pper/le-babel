<%@ page import="dayp308.chatroom.model.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
  created by intellij idea.
  User: dayp308
  Date: 2023/7/24
  Time: 下午5:06
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
	<head>
	    <meta charset="UTF-8">
	    <title>聊天室</title>
	    <link href="<c:url value="/static/css/chat.css"/>" type="text/css" rel="stylesheet">
	    <script src="https://kit.fontawesome.com/158cfb734b.js" crossorigin="anonymous"></script>
	    <%
	        request.setCharacterEncoding("utf-8");
	        response.setCharacterEncoding("utf-8");
	  Object attrUser = session.getAttribute("user");
	        User user = null;
	        if (attrUser instanceof User) {
	            user = (User) attrUser;
	        }
			System.out.println(user);
	    %>
		<script>
			const username = "<%=user.getUsername()%>";
			const userId = "<%=user.getUserId()%>" * 1;
			const avatar = "<%=user.getAvatar()%>" ? "<%=user.getAvatar()%>" : ( self.location.host + "/static/img/default_avatar.png" );
			const token = "<%=user.getToken()%>"
			const server = <%=request.getAttribute("serverInfo")%>;
		</script>
	</head>
	<body>
		<div id="app-chat"></div>
		<script type="importmap">
			{	"imports": {
					"vue": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/vue.esm-browser.js",
					"axios": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/axios.esm.min.js",
					"main-component": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/component/main-component.js"
				}
			}
		</script>
<%--		<script src="<c:url value="/static/js/chat-main.js"/>" type="module"></script>--%>
		<script type="module">
			import axios from 'axios';
			import { createApp, computed } from 'vue';
			import MainComponent from 'main-component'

			const main = MainComponent;
			main.created = function () {
				this.url = "ws://" + self.location.host + "/server/" + server['id'] + "/" + token;
				server['channelList'].forEach(e => {
					this.channels.set(e['channelId'], e['channelName']);
					this.cachedInputs.set(e['channelId'], "")
				})
				server['memberList'].forEach(e => {
					this.members.set(e.userId, {
						"username": e.username,
						"avatar": e.avatar,
						"identity": e.identity,
						"nickname": e.nickname
					})
				})
				this.serverInfo = {
					id: server['id'],
					name: server['name'],
					avatar: server['avatar'],
					description: server['description'],
					banner: server['banner']
				}
				this.username = username;
				this.avatar = avatar;
				this.permission = <%=request.getAttribute("permission")%>
				this.activeChannel = server.channelList[0].channelId;
				this.cachedInput = [this.cachedInputs.values()];
				console.log("channels:")
				console.log(this.channels);
				console.log("members:")
				console.log(this.members)
				console.log("app successfully created");
			}
			main.mounted = function() {
				this.ws = new WebSocket(this.url);
				this.ws.onmessage = (e) => {
					const dataObj = JSON.parse(e.data);
					console.log(dataObj);
					this.$refs['msgWindows'].forEach((e) => {
						if (e.channelId === dataObj.inChannel)
							e.showUsrText(dataObj);
					})
				}
				this.isMounted = true;
				console.log("app successfully mounted");
			}

			const app = createApp(main)
			app.config.globalProperties = {
				dateFmt : new Intl.DateTimeFormat('zh-CN', {dateStyle: "short", timeStyle: "medium"}),
				userId: userId,
			}

			app.mount("#app-chat");
		</script>
	</body>
</html>
