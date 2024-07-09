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
    <script src="<c:url value="/static/js/jquery-3.6.4.js"/>" type="application/javascript"></script>
    <link href="<c:url value="/static/css/chat.css"/>" type="text/css" rel="stylesheet">
    <script src="https://kit.fontawesome.com/158cfb734b.js" crossorigin="anonymous"></script>
    <%
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        Object attrUser = session.getAttribute("user");
        User user = null;
        if (attrUser instanceof User) {
            user = (User) attrUser;
//            System.out.println(user);
        }
    %>
</head>
<body>
<div id="app-chat">
<%--    <div class="chatrooms">--%>
<%--        <div class="avatar-box">--%>
<%--            <div class="box-left-border"></div>--%>
<%--            <div class="avatar"></div>--%>
<%--            <div class="box-right-border"></div>--%>
<%--        </div>--%>
<%--    </div>--%>
    <div class="channels">
        <div class="header">minecraft</div>
        <div class="channel-list">
            <div class="channel" id="cid-2">others</div>
            <div class="channel" id="cid-3">dev</div>
        </div>
        <div class="user-console">
            <div class="user-card">
                <div class="avatar"></div>
                <div class="user-name">
                    <div class="nickname"></div>
                    <div class="user-id"></div>
                </div>
            </div>
            <button id="logout">登出</button>
        </div>
    </div>
    <div class="chatarea">
        <div class="header">#others</div>
        <div class="get-history" ><i class="fa-solid fa-arrow-up"></i></div>
        <ul class="messages" id="msgC2">
        </ul>
        <ul class="messages" id="msgC3">
        </ul>
        <div class="footer">
            <div class="input-bar">
                <textarea class="text" id="input-text" placeholder="给#others 发消息"></textarea>
            </div>
        </div>
    </div>
</div>
<script>
    const dateFmt = new Intl.DateTimeFormat('zh-CN', {dateStyle: "short", timeStyle: "medium"});
    let timestamp = Math.floor(new Date().getTime());
    let user = "<%=user.getUsername()%>";
    let id = "<%=user.getUserId()%>" * 1;
    let avatar = "<%=user.getAvatar()%>" ? "<%=user.getAvatar()%>" : "${pageContext.request.contextPath}/static/img/default_avatar.png";
    let token = "<%=user.getToken()%>"
    $(".user-card .avatar").css("background-image", "url(\"" +avatar + "\")");
    // console.log(token);
    let server = "1";
    let channel = 2;
    const url = "ws://" + getContextPath() + "/server/" + server + "/" + token;
    // console.log(url);
    const ws = new WebSocket(url);
    ws.onmessage = (e) => {
        // console.log(e.data)
        const dataObj = JSON.parse(e.data);
        console.log(dataObj);
        if (dataObj.type === "user") {
            showUsrText(dataObj.text, dataObj.owner, dataObj.date, getMessageNode(dataObj.inChannel), "append");
        }
        else if ( dataObj.type === "sys" ) {
            showSysMsg(dataObj.text, getMessageNode("sys"));
        }
    };
    function showSysMsg(text, node) {
        const $li = $(
            "<li class='systemMessage'>"
            + "- " + text + " -"
            + "</li>"
        );
        node.append($li);
        goBottom();
    }
    function getMessageNode(channel_id) {
        return $( ("#msgC" + channel_id) );
    }
    function showUsrText(text, owner, time, node, direction) {
        const date = new Date(time * 1 );
        $.ajax({
            type: 'POST',
            url: "${pageContext.request.contextPath}/get_username",
            data: {userId: owner, serverId: server},
            dataType: "json",
            success: (msgObj) => {
                console.log(msgObj);
                let avatar = msgObj.avatar ? msgObj.avatar : "${pageContext.request.contextPath}/static/img/default_avatar.png"
                const $li = $(
                      "<li class='" + ( owner === id ? "myMessage" : "otherMessage" ) + "'>"
                    +   "<img class='avatar' src='" + avatar + "' alt='浏览器不支持!'>"
                    +   "<div class='literalMsg'>"
                    +     "<div class='timeAndName'>"
					+		"<span class='user-title " + msgObj.identity + "'>"
					+			( msgObj.identity == "owner" ? "群主" :
								msgObj.identity == "admin" ? "管理员" : "" ) + " </span>"
                    +       "<span class='username'>" + msgObj.nickname + "</span>"
                    +	    "<span class='time'>" + dateFmt.format(date) + "</span>"
                    +     "</div>"
                    +     "<div class='msgText'>" + text + "</div>"
                    +   "</div>"
                    + "</li>");
                if (direction === "append") {
                    node.append($li);
                    if ( owner === id )
                        goBottom();
                }
                else if (direction === "prepend") {
                    node.prepend($li);
                    goTop();
                }
            },
            error: (data) => {
                console.log(data);
            }
        });
    }
    function getToken() {
        const arrCookies = document.cookie.split(";");
        arrCookies.forEach((e) => {
            const value = e.trim();
            if (value.indexOf("token=") === 0) token = value.substring("token=".length, value.length);
        })
        return token;
    }
    function goBottom() {
        const messages = $(".messages");
        const height = messages.prop("scrollHeight");
        messages.scrollTop(height);
    }
    function goTop() {
        $(".messages").scrollTop(0);
    }
    $(() => {
        $("#cid-2").attr("class", "active-channel");
        $("#msgC3").hide();
        $(".user-card .nickname").html(user);
        $(".user-card .user-id").html(user);
        $("#logout").on("click", (e) => {
            if (confirm("确定要登出吗？")) {
                const d = new Date(0);
                document.cookie = "token=;expires=" + d.toUTCString() + ";path=/;";
                window.location = "/login"
            }
        });
        $(".channel").on("click", (e) => {
            const id = $(e.target).attr("id").split("-")[1];
            $(e.target).siblings().attr("class", "channel");
            $(e.target).attr("class", "active-channel");
            $("#msgC2").hide();
            $("#msgC3").hide();
            $( ("#msgC" + id) ).show();
            $("#input-text").attr("placeholder", "给 #"+$(e.target).text()+" 发消息")

            channel = id;
        });
        $(".get-history").on("click", (e) => {
            console.log(timestamp);
            $.ajax({
                type: "POST",
                url: "${pageContext.request.contextPath}/get_msg_history",
                data: {
                    channel: channel,
                    timestamp: timestamp,
                },
                dataType: 'json',
                success: (data) => {
                    // console.log(data);
                    // console.log("current timestamp is: " + timestamp);
                    data.forEach((e)=>{
                        showUsrText(e.text, e.owner, e.date, getMessageNode(e.inChannel), "prepend");
                    });

                    if (data.at(-1)) {
                        timestamp = data.at(-1).date - 1000;
                        // console.log("timestamp changed to: " + timestamp);
                    }
                    else {
                        timestamp = timestamp - 30 * 60 * 1000;
                    }
                },
                error: (data) => {
                    if (!data)
                        timestamp = timestamp - 30 * 60 * 1000;
                    // console.log("error data: " + data + ", timestamp changed to: " + timestamp);
                }
            });
        });
        $(".text").keydown( (e) => {
            if ( e.keyCode === 13 ) {
                const text = $(".text").val().trim()
                const json = JSON.stringify({
                    text: text,
                    file:"",
                    channel: channel,
                    server: server
                });

                if (text !== "") {
                    // console.log(text);
                    ws.send(json);
                    $(".text").val("");
                }
                else alert("文本不能为空！");
                e.preventDefault();
            }
        })
    });
    function getContextPath() {
        return self.location.host;
    }
</script>
</body>
</html>
