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
    <title>登录</title>
    <script src="<c:url value="/static/js/jquery-3.6.4.js"/>"></script>
    <link href="<c:url value="/static/css/login.css"/>" rel="stylesheet">
</head>
<body>
<div id="app-login">
    <div id="login-layer">
        <div class="login-box">
            <div class="header">欢迎！请登录</div>
            <form method="post" action="${pageContext.request.contextPath}/check_login">
            <label class="important" for="username">
                <span>用户名</span>
                <input class="text-box" type="text" name="username" id="username">
            </label>
            <label class="important">
                <span>密码</span>
                <input class="text-box" type="password" name="password" id="password">
            </label>
            <div class="check-box">
                <input type="checkbox" id="remember" name="remember" value="true">
                <label for="remember">记住登录状态</label>
            </div>
            <div>没有账号？<a href class="to-register">注册</a></div>
            <button id="submit-login" class="submit" >登录</button>
            </form>
        </div>
    </div>
    <div id="register-layer">
        <div class="register-box">
            <div class="header">注册</div>
            <form method="post" action="${pageContext.request.contextPath}/add_user">
            <label for="uname" class="important">
                <span>用户名</span>
                <input class="text-box" type="text" name="username" id="uname">
            </label>
            <label for="pwd" class="important">
                <span>密码</span>
                <input class="text-box" type="password" name="password" id="pwd">
            </label>
            <label for="repeat-pwd" class="important">
                <span>确认密码</span>
                <input class="text-box" type="password" id="repeat-pwd">
            </label>
            <label>
                <span>出生日期</span>
                <input type="date" name="birthday" id="birthday">
            </label>
            <div>已有账号？<a href class="to-login">登录</a></div>
            <span class="warn"></span>
            <button id="submit-register" class="submit">注册</button>
            </form>
        </div>
    </div>
</div>
<script type="application/javascript">
    $(()=> {
        if (window.location.pathname === "/login") {
            $("#register-layer").css("display", "none");
            $("#login-layer").css("display", "inherit");
        }
        else if (window.location.pathname === "/register") {
            $("#login-layer").css("display", "none");
            $("#register-layer").css("display", "inherit");
        }
    })
    $(".to-register").on("click", (e) => {
        e.preventDefault();
        history.pushState({}, "",  "/register");
        $("#login-layer").fadeOut("fast");
        $("#register-layer").fadeIn("fast");
    })
    $(".to-login").on("click", (e) => {
        e.preventDefault();
        history.pushState({}, "",  "/login");
        $("#register-layer").fadeOut("fast");
        $("#login-layer").fadeIn("fast");
    })
    $("#pwd, #repeat-pwd").on("change", (e) => {
        if ( $("#pwd").val() !== $("#repeat-pwd").val() ) {
            $(".warn").html("两次密码不一致！");
        }
        else $(".warn").html("");

    })
</script>
</body>
</html>
