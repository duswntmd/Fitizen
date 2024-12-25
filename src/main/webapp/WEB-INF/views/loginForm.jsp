<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/css/users/loginForm.css">
</head>
<body>
<header>
    <%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
</header>
<div class="page-contents">
<!-- 로그인 폼을 감싸는 div에 id 부여 -->
<div id="login-form-container">
    <form action="<c:url value='/dologin'/>" method="post" onsubmit="return formCheck(this);">
        <h3>로그인</h3>
        <div id="msg">
            <c:if test="${not empty param.msg}">
                <i class="fa fa-exclamation-circle"> ${URLDecoder.decode(param.msg)}</i>
            </c:if>
        </div>
        <input type="text" name="id" value="${cookie.id.value}" placeholder="아이디 입력" autofocus>
        <input type="password" name="pwd" placeholder="비밀번호">
        <input type="hidden" name="toURL" value="${param.toURL}">
        <button type="submit">로그인</button>
<%--        <div class="remember-me">--%>
<%--            <label><input type="checkbox" name="rememberId" value="on" ${empty cookie.id.value ? "" : "checked"}> 아이디 기억</label>--%>
<%--        </div>--%>
        <div class="footer-links">
            <a href="/mail/findId">아이디 찾기</a> |
            <a href="/mail/findPwd">비밀번호 찾기</a>
        </div>
    </form>
</div>
</div>
<footer>
    <%@ include file="footer.jsp" %>
</footer>

<script src="/js/users/loginForm.js"></script>
</body>

</html>