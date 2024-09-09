<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false"%>
<c:set var="loginId" value="${pageContext.request.getSession(false)==null ? '' : pageContext.request.session.getAttribute('id')}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? '로그인' : 'ID='+=loginId}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사용자 삭제</title>

</head>
<body>

<form action="/register/deleteuser" method="post">
    <h2>사용자 삭제</h2>
    <%-- 에러 메시지 출력 --%>
    <% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <%-- 성공 메시지 출력 --%>
    <% if (request.getAttribute("message") != null) { %>
    <p style="color: green;"><%= request.getAttribute("message") %></p>
    <% } %>
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



<script>
    let msg = "${msg}";
    if(msg=="DEL_OK")    alert("회원탈퇴가 성공적으로 삭제되었습니다.");

</script>
</body>
</html>
