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
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->



<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FITIZEN</title>
</head>
<body>
<div class="content-wrapper">
    <!-- 페이지 컨텐츠 -->
    <div style="text-align:center">
        <h1 style="line-height: 0.4; color:white">
            <p style="line-height: 0;">FITNESS + CITIZEN</p>
            <p style="line-height: 0; font-size: 150%;">FITIZEN</p>
        </h1>
        <h3 style="line-height: 50%; color:white">
            <p style="line-height: 0;">피트니스를 애용하는 헬린이들을 위한 소통창구</p>
            <p style="font-size: 135%;">피티즌 메인 페이지입니다.</p>
        </h3>
    </div>
    <div class="extra" style="color: white;">

        <%@ include file="carousel.jsp" %>
</div>


<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
<style>


    html, body {
        height: auto;
        margin: 0; /* 기본 여백 제거 */
        overflow-x: hidden; /* 가로 스크롤 방지 */
    }

    .content-wrapper {
        flex-grow: 1;
        width: 100%;
        background: url("../image/Fitizen-Background.png") no-repeat center center;
        background-size: cover; /* 배경 이미지를 화면에 맞춤 */
    }

    body {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        min-height: 100vh;
    }
    h1 {
        font-family: 'Pretendard-Black';
        font-size : 400%;

    }
    h2 {
        font-family: 'Pretendard-Black';
        font-size : 600%;
    }
    h3 {
        font-family: 'Pretendard-Black';
        font-size : 200%;
    }
    h4 {
        font-family: 'Pretendard-Black';
        font-size : 300%;
    }


</style>
</html>
