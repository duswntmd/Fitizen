<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png"/>
    <title>사용자 삭제</title>
    <link rel="stylesheet" type="text/css" href="/css/users/deleteUser.css">
</head>
<body>
<input type="hidden" name="_csrf" value="${_csrf.token}" />
<div id="bodyContainer">
<div class="page-contents">
    <h2>사용자 삭제</h2>
    <form id="deleteUserForm">
        <label for="id">아이디:</label>
        <input type="text" id="id" name="id" required>
        <br>
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required>
        <br>
        <label for="pwd">비밀번호:</label>
        <input type="password" id="pwd" name="pwd" required>
        <br>
        <button type="submit">사용자 삭제</button>
    </form>
</div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script src="/js/users/deleteUser.js"defer></script>

<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>
