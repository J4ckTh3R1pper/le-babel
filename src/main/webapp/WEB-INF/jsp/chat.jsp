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
			const avatar = "<%=user.getAvatar()%>" ? "<%=user.getAvatar()%>" : ( getContextPath() + "/static/img/default_avatar.png" );
			const token = "<%=user.getToken()%>"
			const server = 1;
			const channel = 2;
		</script>
	</head>
	<body>
		<div id="app-chat"></div>
		<script type="importmap">
			{	"imports": {
					"vue": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/vue.esm-browser.js",
					"axios": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/axios.esm.min.js"
				}
			}
		</script>
		<script src="<c:url value="/static/js/chat-main.js"/>" type="module"></script>
	</body>
</html>
