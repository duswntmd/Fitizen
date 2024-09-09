<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true"%>
<c:set var="loginId" value="${pageContext.request.getSession(false)==null ? '' : pageContext.request.session.getAttribute('id')}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? '로그인' : 'ID='+=loginId}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사용자 정보 수정</title>

</head>

<div id="page-wrap">


<body>

<form action="${pageContext.request.contextPath}/register/updateuser" method="post">
    <h2>사용자 정보 수정</h2>
    <c:if test="${not empty message}">
        <p style="color: green;">${message}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    <input type="hidden" name="id" value="${user.id}" />
    <label>ID: <span>${user.id}</span></label><br>
    <label>비밀번호: <input type="password" name="pwd" value="${user.pwd}" /></label><br>
    <label>이름: <input type="text" name="name" value="${user.name}" /></label><br>
    <label>이메일: <input type="email" name="email" value="${user.email}" /></label><br>
    <label>생년월일: <input type="date" name="birth" value="${user.birth}" /></label><br>
    <input type="submit" value="수정하기">
</form>

<script>
    let msg = "${msg}";
    if(msg=="WRT_OK")    alert("회원수정이 성공적으로 수정되었습니다.");
</script>
</body>
</html>
