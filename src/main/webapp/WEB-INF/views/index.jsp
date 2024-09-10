<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true"%> <!-- 세션 사용을 위해 session="true"로 변경 -->
<%-- "user" 객체가 세션에 있는지 확인하고, 로그인 상태 및 로그아웃 링크를 설정 수정수정 --%>
<c:set var="user" value="${sessionScope.user}" />
<c:set var="loginId" value="${user == null ? '' : user.id}" />
<c:set var="loginOutLink" value="${user == null ? '/login/login' : '/login/logout'}" />

<%-- 문자열 결합을 명시적으로 처리 --%>
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
    <title>FITIZEN</title>
</head>
<body>
<div id="menu">
    <ul>
        <li><a href="/qna">Q&A</a></li>
        <li id="logo"><a href="/"><img src="img/logo.png" width="200" height="100"></a></li>
        <li><a href="/findME">맞춤 운동 찾기</a></li>
        <li><a href="/board/list">Boardlist</a></li>
        <li><a href="${loginOutLink}">${loginOut}</a></li> <!-- 로그인/로그아웃 링크 -->
        <li><a href="/register/add">회원가입</a></li>
        <li><a href="/register/updateuser">수정</a></li>
        <li><a href="/register/deleteuser">삭제</a></li>
        <li><a href="/kakao/map">지도</a></li>
        <li><a href="/challenge">챌린지</a></li>

    </ul>
</div>

<div style="text-align:center">
    <h1>This is FITIZEN HOME</h1>
</div>
</body>
</html>
