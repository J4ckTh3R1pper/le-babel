<%@ page import="dayp308.chatroom.bean.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
  Created by IntelliJ IDEA.
  User: dayp308
  Date: 2024/7/23
  Time: 下午8:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Title</title>
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
    <style type="text/css">
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
    </style>
  </head>
  <body>
    <div id="app"></div>
    <script type="importmap">
	  {  "imports": {
		   "vue": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/vue.esm-browser.js",
		   "axios": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/axios.esm.min.js",
		   "file-manager": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/component/file-manager.js"
	     }
	  }
	</script>
    <script type="module">
        import {createApp} from "vue"
        import axios from "axios"
        import fileManager from "file-manager";
        const serverId = <%=request.getAttribute("serverId")%>
        const main = fileManager;
        main.created = function () {
            this.serverId = serverId;
            axios.get("/list_files", {
                params: {
                    serverId: this.serverId,
                }
            }).then(resp => {
                this.filePage = resp.data;
            })
        }
        const app = createApp(main);
        app.mount("#app")
    </script>
  </body>
</html>
