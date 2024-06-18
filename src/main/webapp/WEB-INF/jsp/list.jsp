<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dayp308.chatroom.bean.Identity" %>
<%@ page import="dayp308.chatroom.bean.User" %>
<%--
  Created by IntelliJ IDEA.
  User: dayp308
  Date: 2023/11/7
  Time: 下午5:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    Object attrUser = session.getAttribute("user");
    User loginUser = null;
    if (attrUser instanceof User) {
        loginUser = (User) attrUser;
//            System.out.println(loginUser);
    }
%>
<html>
    <head>
        <title>用户列表</title>
        <script src="<c:url value="/static/js/jquery-3.6.4.js"/>" type="application/javascript"></script>
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
    <jsp:useBean id="user" scope="session" type="dayp308.chatroom.bean.User"/>
        <div>群号: ${serverId}</div>
        <label>
            <input type="text" id="searchText" value="${searchText}">
        </label>
        <button id="search">搜索</button>
        <table id="list_users">
            <tr>
                <th>用户ID</th>
                <th>用户名</th>
                <th>身份</th>
                <th>群内昵称</th>
                <th>选项</th>
            </tr>
            <c:forEach
                    items="${serverMembers.list}" var="member" varStatus="userStatus">
                <tr class="search_result">
                    <td>${member.user.userId}</td>
                    <td>${member.user.username}</td>
                    <!--<td><fmt:formatDate value="${member.user.birthday}" pattern="yyyy-MM-dd"/></td> -->

                    <td>${member.identity == Identity.OWNER ? "群主" : member.identity == Identity.ADMIN ? "管理员" : "普通成员" }</td>
                    <td>${member.nickname}</td>
                    <td>
                        <%-- <a href="${pageContext.request.contextPath}/edit_user?id=${loginUser.userId}">修改</a> --%>
						<c:if test="${permission > member.identity.index}">
							<button class="banish-user"
                                    id="banishUser_${member.user.userId}"
                                    data-user-id="${member.user.userId}"
                                    data-username="${member.user.username}"
                                    data-href="${pageContext.request.contextPath}/banish_user?userId=${member.user.userId}&server=${serverId}">
                                踢出
                            </button>
						</c:if>
                        <c:if test="${permission > member.identity.index || member.user.userId == user.userId }">
                            <button class="set-nickname"
                                    id="setNickname_${member.user.userId}"}
                                    data-username="${member.user.username}"
                                    data-user-id="${member.user.userId}">
                                更改群内昵称
                            </button>
                        </c:if>
						<c:if test="${permission == 2 && member.identity.index == 0}">
							<button class="set-admin"
                                    data-user-id="${member.user.userId}"
									data-username="${member.user.username}"
                                    data-href="${pageContext.request.contextPath}/set_admin?userId=${member.user.userId}&serverId=${serverId}">
                                设为管理员
                            </button>
						</c:if>
						<c:if test="${permission == 2 && member.identity.index == 1}">
							<button class="unset-admin"
                                    data-user-id="${member.user.userId}"
									data-username="${member.user.username}"
                                    data-href="${pageContext.request.contextPath}/unset_admin?userId=${member.user.userId}&serverId=${serverId}">
                                取消管理员
                            </button>
						</c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <div class="bottom">共${serverMembers.total}条结果 <button id="prevPage">上一页</button> <button id="nextPage">下一页</button> <button id="jumpPage">跳转至</button>第<input id="goToPage" type="number" value="${serverMembers.pageNum}">/${serverMembers.pages}页</div>
        <div>
            <label>
                添加新成员：<input id="userIdToAdd" type="text">
            </label>
            <button id="addUser">添加</button>
        </div>
        <dialog id="nicknameDialog">
            <label for="nicknameInput">设置用户<span id="nicknameUser"></span>"的群内昵称：</label><input id="nicknameInput" type="text">
            <button id="submitNickname">设置</button>
            <button id="closeNicknameDialog">取消</button>
        </dialog>
        <script type="application/javascript">
            $(document).ready((e) => {
                $("#search").on("click", (e) => {
                    e.preventDefault()
                    $(location).attr("href",
                            "${pageContext.request.contextPath}/list_users?"
                        +   "searchText=" + $("#searchText").val()
                        +   "&serverId=" + ${serverId}
                    )
                })

                $("#addUser").on("click", (e) => {
                    e.preventDefault()
                    $(location).attr("href",
                        "${pageContext.request.contextPath}/invite_user?"
                        +   "userId=" + $("#userIdToAdd").val()
                        +   "&serverId=" + ${serverId}
                    )
                })

                $(".banish-user").on("click", (e) => {
                    e.preventDefault();
                    let t = e.target;
                    if ( confirm("确定要踢出用户" + t.dataset.username + "(ID: " + t.dataset.userId + ")吗？" ) )
                        $(location).attr("href", t.dataset.href)
                })

                $(".set-nickname").on("click", (e) => {
                    e.preventDefault();
                    let t = e.target;
                    $("#nicknameDialog").data("username", t.dataset.username);
                    $("#nicknameDialog").data("userId", t.dataset.userId);
                    $("#nicknameDialog #nicknameUser").text( t.dataset.username + "(ID: " + t.dataset.userId + ")")
                    $("#nicknameDialog")[0].showModal();
                })

                $("#closeNicknameDialog").on("click", (e) => {
                    $("#nicknameDialog")[0].showModal();
                })

                $("#submitNickname").on("click", (e) => {
                    let t = $("#nicknameDialog")
                    e.preventDefault();
                    $(location).attr("href",
                                    "${pageContext.request.contextPath}/set_nickname?userId="
                                    + t.data('userId')
                                    + "&serverId=${serverId}"
                                    + "&nickname=" + $("#nicknameInput").val().trim()
                    )
                })
                $(".set-admin").on("click", (e) => {
                    e.preventDefault();
                    let t = e.target;
                    if ( confirm("确定要设置用户" + t.dataset.username + "(ID: " + t.dataset.userId + ")为管理员吗？" ) )
                        $(location).attr("href", t.dataset.href)
                })

                $(".unset-admin").on("click", (e) => {
                    e.preventDefault();
                    let t = e.target;
                    if ( confirm("确定要取消用户" + t.dataset.username + "(ID: " + t.dataset.userId + ")的管理员权限吗？" ) )
                        $(location).attr("href", t.dataset.href)
                })

                $("#prevPage").on("click", (e) => {
                    e.preventDefault()
                    let hasPrevPage = ${serverMembers.hasPreviousPage};
                    if ( hasPrevPage )
                        $(location).attr("href",
                                "${pageContext.request.contextPath}/list_users?"
                            +   "page=${serverMembers.pageNum - 1}"
                            +   "&searchText=" + "${searchText}"
                            +   "&serverId=" + ${serverId}
                        )
                })

                $("#nextPage").on("click", (e) => {
                    e.preventDefault()
                    let hasNextPage = ${serverMembers.hasNextPage};
                    if ( hasNextPage )
                        $(location).attr("href",
                                "${pageContext.request.contextPath}/list_users?"
                            +   "page=${serverMembers.pageNum + 1}"
                            +   "&searchText=" + "${searchText}"
                            +   "&serverId=" + ${serverId}
                        )
                })

                $("#jumpPage").on("click", (e) => {
                    e.preventDefault()
                    $(location).attr("href",
                            "${pageContext.request.contextPath}/list_users?"
                        +   "page=" + $("#goToPage").val()
                        +   "&searchText=" + "${searchText}"
                        +   "&serverId=" + ${serverId}
                    )
                })
            })
        </script>
    </body>
</html>
