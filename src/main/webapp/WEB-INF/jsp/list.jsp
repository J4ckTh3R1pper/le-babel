<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dayp308.chatroom.bean.Identity" %>
<%--
  Created by IntelliJ IDEA.
  User: dayp308
  Date: 2023/11/7
  Time: 下午5:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                        <%-- <a href="${pageContext.request.contextPath}/edit_user?id=${user.userId}">修改</a> --%>
                        <button id="banishUser" data-user-id="${member.user.userId}" data-username="${member.user.username}" data-href="${pageContext.request.contextPath}/banish_user?userId=${member.user.userId}&server=${serverId}">踢出</button>
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
        <script type="application/javascript">
            $(document).ready((e) => {
                $("#search").on("click", (e) => {
                    e.preventDefault()
                    $(location).attr("href",
                            "${pageContext.request.contextPath}/list_users?"
                        +   "searchText=" + $("#searchText").val()
                        +   "&server=" + ${serverId}
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

                $("#banishUser").on("click", (e) => {
                    e.preventDefault();
                    let t = e.target;
                    if ( confirm("确定要踢出用户" + t.dataset.username + "(ID: " + t.dataset.userId + ")吗？" ) )
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
                            +   "&server=" + ${serverId}
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
                            +   "&server=" + ${serverId}
                        )
                })

                $("#jumpPage").on("click", (e) => {
                    e.preventDefault()
                    $(location).attr("href",
                            "${pageContext.request.contextPath}/list_users?"
                        +   "page=" + $("#goToPage").val()
                        +   "&searchText=" + "${searchText}"
                        +   "&server=" + ${serverId}
                    )
                })
            })
        </script>
    </body>
</html>
