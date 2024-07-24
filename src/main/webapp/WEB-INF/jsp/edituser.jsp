<%@ page import="dayp308.chatroom.bean.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: dayp308
  Date: 2023/12/17
  Time: 下午11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>编辑用户</title>
</head>
<body>
    <div id="app">
        <label>
            用户ID: {{userData.userId}}
        </label><br>
        <label>
            用户名
            <input type="text" name="username" ref="username" v-model="userData.username">
        </label><br>
        <label>
            新密码
            <input type="password" name="password" ref="pwd">
        </label><br>
        <label>
            重复新密码
            <input type="password" ref="repeatPwd">
        </label><br>
        <label>
            头像
            <img :src="userData.avatar">
            <input type="file" name="avatar" ref="avatar"
                   accept="image/png, image/jpeg">
            <button @click="updateAvatar">更改头像</button>
        </label><br>
        <label>
            生日
            <input type="date" name="birthday" ref="birthday" v-model="userData.birthday">
        </label><br>
        <button @click="updateUser">保存</button>
    </div>
    <script type="importmap">
			{	"imports": {
					"vue": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/vue.esm-browser.js",
					"axios": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/axios.esm.min.js",
					"main-component": "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/static/js/component/main-component.js"
				}
			}
	</script>
    <script type="module">
        import {createApp} from "vue";
        import axios from "axios";

        const userData = <%=request.getAttribute("userData")%>;
        const main = {
            data() {
                return {
                    userData: userData
                }
            },
            methods: {
                updateAvatar() {
                    let formData = new FormData();
                    formData.append("image", this.$refs['avatar'].files[0]);
                    formData.append("token", "<%=((User) session.getAttribute("user")).getToken()%>");
                    axios.post("/edit_user/update_avatar", formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        },
                    }).then(e => {
                        axios.get("get_user?userId=" + this.userData.userId)
                            .then(e => {
                                this.userData = e.data;
                            })
                    })
                },
                updateUser() {
                    if (this.$refs['pwd'].value === this.$refs['repeatPwd'].value)
                    axios.post("/edit_user/update_user", {
                        userId: this.userData.userId,
                        username: this.$refs['username'].value,
                        password: this.$refs['pwd'].value,
                        birthday: this.$refs['birthday'].value
                    }).then(e => {
                        axios.get("get_user?userId=" + this.userData.userId)
                            .then(e => {
                                this.userData = e.data;
                            })
                    })
                }
            }
        }
        const app = createApp(main);
        app.mount("#app");
    </script>
  </body>
</html>
