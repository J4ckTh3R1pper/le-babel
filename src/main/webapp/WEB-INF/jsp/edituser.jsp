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
    <script type="application/javascript" src="<c:url value="/static/js/jquery-3.6.4.js"/>"></script>
</head>
<body>
    <form id="new_user" action="${pageContext.request.contextPath}/app/edit_user/updateUser" method="post">
        <label>
            用户ID: ${userData.userId}
        </label><br>
        <label>
            用户名
            <input type="text" name="username" id="username">
        </label><br>
        <label>
            新密码
            <input type="password" name="password" id="pwd">
        </label><br>
        <label>
            重复新密码
            <input type="password" id="repeatpwd">
        </label><br>
        <label>
            头像
            <input type="text" name="avatar" id="avatar">
        </label><br>
        <label>
            生日
            <input type="date" name="birthday" id="birthday">
        </label><br>
        <button id="submit" type="submit">保存</button>
    </form>
<script>
    $(document).ready( (e) => {
        $("#birthday").val("<fmt:formatDate value="${userData.birthday}" pattern="yyyy-MM-dd"/>")
        $("#username").val("${userData.username}")
        $("#avatar").val("${userData.avatar}")
        $("#submit").on("click", (e) => {
            e.preventDefault()
            if ( $("#pwd").val() == null || $("#pwd").val() != null && $("#pwd").val() === $("#repeatpwd").val() )
            {
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/app/edit_user/update_user",
                    data: JSON.stringify({
                        userId: ${userData.userId},
                        username: $("#username").val(),
                        password: $("#pwd").val(),
                        avatar: $("#avatar").val(),
                        birthday: $("#birthday").val()
                    }),
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    success: (data) => {
                        console.log(data)
                        if ( data === 3 )
                            alert("该用户不存在！")
                        else if ( data === 2 )
                            alert("用户名已被使用！")
                        else if ( data === 101 )
                            alert("未知错误！")
                        else if ( data === 100 )
                            $(location).attr("href", "${pageContext.request.contextPath}/app/list_users")
                    }
                })

            }

        })
    } )
</script>
</body>
</html>
