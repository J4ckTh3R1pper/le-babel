<%@ page import="dayp308.chatroom.bean.User" %>
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
			const channelList = <%=request.getAttribute("channelList")%>
			const server = <%=request.getAttribute("serverId")%>;
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
			import { createApp } from 'vue';
			import MainComponent from 'main-component'

			const main = MainComponent;
			main.created = function () {
				this.url = "ws://" + self.location.host + "/server/" + server + "/" + token;
				channelList.forEach(e => {
					this.channels.set(e.id, e.name);
					this.cachedInputs.set(e.id, "")
				})
				this.activeChannel = channelList[0].id;
				this.cachedInput = [this.cachedInputs.values()];
				console.log(this.channels);
				console.log(this.cachedInput);
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
				console.log("app successfully mounted");
			}

			const app = createApp(main)
			app.config.globalProperties = {
				dateFmt : new Intl.DateTimeFormat('zh-CN', {dateStyle: "short", timeStyle: "medium"}),
				serverId: server,
				userId: userId,
			}

			app.mount("#app-chat");
		</script>
	</body>
</html>
