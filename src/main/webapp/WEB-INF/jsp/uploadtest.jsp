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
    <div id="app">
        <input type="file" ref="input">
        <button @click="upload">上传</button>
        <table id="list-files">
            <tr>
                <th>文件名</th>
                <th>上传日期</th>
                <th>上传者ID</th>
                <th>操作</th>
            </tr>
            <template v-for="file in files" :key="file.fileId">
                <tr>
                    <td>{{file.fileName}}</td>
                    <td>{{file.uploadDate}}</td>
                    <td>{{file.userId}}</td>
                    <td>
                        <button @click="download(file.fileId)">下载</button>
                    </td>
                </tr>
            </template>
        </table>
    </div>
    <script type="importmap">
	  {  "imports": {
		   "vue": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/vue.esm-browser.js",
		   "axios": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/axios.esm.min.js",
		   "main-component": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/component/main-component.js"
	     }
	  }
	</script>
    <script type="module">
        import {createApp} from "vue"
        import axios from "axios"
        const serverId = <%=request.getAttribute("serverId")%>
        const fileList = eval(<%=request.getAttribute("fileList")%>);
        const mainComponent = {
            data() {
                return {
                    serverId: serverId,
                    files: fileList
                }
            },
            mounted() {
              console.log(this.files);
            },
            methods: {
                download(fileId) {
                    window.open("/download?fileId=" + fileId);
                },
                async upload() {
                    if (!this.$refs['input'].files)
                        return;
                    const data = new FormData();
                    data.append("file", this.$refs['input'].files[0]);
                    data.append("serverId", this.serverId);
                    let resp;
                    try {
                        resp = await axios.post("/upload_file", data, {
                                headers: {
                                    'Content-Type': 'multipart/form-data'
                                },
                                transformRequest: [function (data, headers) {
                                    console.log(headers)
                                    return data;
                                }]
                        })
                    } catch (e) {
                        console.log(e);
                    }
                    finally {
                        console.log(resp);
                    }
                }
            }
        }
        const app = createApp(mainComponent);
        app.mount("#app")
    </script>
  </body>
</html>
