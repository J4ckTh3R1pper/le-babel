<%@ page import="dayp308.chatroom.bean.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: dayp308
  Date: 2023/7/24
  Time: 下午4:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>管理群</title>
    <script src="<c:url value="/static/js/jquery-3.6.4.js"/>"></script>
    <link href="<c:url value="/static/css/manage.css"/>" rel="stylesheet">
    <style>
        #login-layer {
            display: inline-block;
        }
        table {
            border: black solid 1px;
        }
        td {
            width: 200px;
            border: #000000 solid 1px;
        }
        th {
            border: #000000 solid 2px;
            background-color: #FF0000;
        }
        img {
            max-width: 200px;
        }
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div id="app-login">
    <div id="login-layer">
        <div class="login-box">
            <div id="server-info-app" class="form" v-cloak></div>
            <div id="user-list-app"  class="form" v-cloak></div>
            <div id="file-manager-app"  class="form" v-cloak></div>
        </div>
    </div>
</div>
<script type="importmap">
			{	"imports": {
					"vue": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/vue.esm-browser.js",
					"axios": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/axios.esm.min.js",
					"user-list": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/component/user-list.js",
		            "file-manager": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/component/file-manager.js",
		            "server-info": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/component/server-info.js"
				}
			}
	    </script>
<script type="module">
    import { createApp } from "vue";
    import axios from "axios";
    import serverManager from "user-list";
    import fileManager from "file-manager";
    import serverInf from "server-info";

    const serverInfo = <%=request.getAttribute("serverInfo")%>
    const userId = <%=((User)session.getAttribute("user")).getUserId()%>
    const token = "<%=((User) session.getAttribute("user")).getToken()%>"
    const permission = <%=request.getAttribute("permission")%>

    const userComponent = serverManager;
    const fileComponent = fileManager;
    const serverInfoComponent = serverInf;

    serverInfoComponent.created = function() {
        this.serverInfo = serverInfo;
        this.token = token;
        // this.updateInfo();
    }

    userComponent.created = function () {
        this.permission = permission;
        this.userId = userId;
        this.serverInfo = serverInfo;
        console.log(this.serverInfo)
        axios.get("/list_users?serverId=" + this.serverInfo.id).then(resp => {
            this.userPage = resp.data;
            this.toPage = resp.data['pageNum'];
        })
    };

    fileComponent.created = function () {
        this.serverId = serverInfo.id;
        axios.get("/list_files", {
            params: {
                serverId: this.serverId,
            }
        }).then(resp => {
            this.filePage = resp.data;
            this.toPage = resp.data['pageNum'];
        })
    }
    const fileManagerApp = createApp(fileComponent);
    const userListApp = createApp(userComponent);
    const infoApp = createApp(serverInfoComponent);
    userListApp.mount("#user-list-app");
    fileManagerApp.mount("#file-manager-app");
    infoApp.mount("#server-info-app");
</script>
</body>
</html>
