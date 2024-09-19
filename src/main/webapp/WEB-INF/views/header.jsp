<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true"%>

<c:set var="user" value="${sessionScope.user}" />
<c:set var="loginId" value="${user == null ? '' : user.id}" />
<c:set var="loginOutLink" value="${user == null ? '/login/login' : '/login/logout'}" />

<c:choose>
    <c:when test="${user == null}">
        <c:set var="loginOut" value="Login" />
    </c:when>
    <c:otherwise>
        <c:set var="loginOut" value="${'Logout ('}${loginId}${')'}" />
    </c:otherwise>
</c:choose>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="/css/index/indexStyle.css"> <!-- CSS 파일 링크 -->
</head>
<body>
<header>
    <div class="header-container">
        <div id="logo">
            <a href="/"><img src="/image/logo.png" width="300" height="100" alt="Logo"></a>
        </div>
        <ul class="nav">
            <li><a href="/challenge">챌린지</a></li>
            <li class="dropdown">
                <a href="/board/list">게시판</a>
                <ul class="dropdown-menu">
                    <li><a href="/qna">Q&A</a></li>
                </ul>
            </li>
            <li><a href="${loginOutLink}">${loginOut}</a></li>
            <li><a href="/register/add">회원가입</a></li>
            <li class="dropdown">
                <c:if test="${user != null}">

                    <a href="/user/myPage">마이페이지</a>
                </c:if>

                <ul class="dropdown-menu">
                    <li class="flex_item"><a href="/register/updateuser">수정</a></li>
                    <li class="flex_item"><a href="/register/deleteuser">삭제</a></li>
                </ul>
            </li>



        </ul>
    </div>
</header>
<!-- 페이지 컨텐츠 -->
</body>
</html>