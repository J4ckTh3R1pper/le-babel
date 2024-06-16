<%--
  Created by IntelliJ IDEA.
  User: dayp308
  Date: 2023/7/24
  Time: 下午5:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录成功</title>
    <%=session.getAttribute("username")%>>
</head>
<body>
    欢迎！ <%=session.getAttribute("username")%>
</body>
</html>
