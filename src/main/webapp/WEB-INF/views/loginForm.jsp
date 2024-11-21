<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->

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
<div class="page-contents">
<form action="<c:url value="/dologin"/>" method="post" onsubmit="return formCheck(this);">

    <h3 style="text-align:center">Login</h3>
    <div id="msg">
        <c:if test="${not empty param.msg}">
            <i class="fa fa-exclamation-circle"> ${URLDecoder.decode(param.msg)}</i>
        </c:if>
    </div>
    <input type="text" name="id" value="${cookie.id.value}" placeholder="아이디 입력" autofocus>
    <input type="password" name="pwd" placeholder="비밀번호">
    <input type="hidden" name="toURL" value="${param.toURL}">
    <p>
        <button>로그인</button>
    </p>
    <div>
        <p>
            <label><input type="checkbox" name="rememberId" value="on" ${empty cookie.id.value ? "":"checked"}> 아이디 기억</label>
        </p>
        <a href="/mail/findId">아이디 찾기</a> |
        <a href="/mail/findPwd">비밀번호 찾기</a>
    </div>

    <script src="/js/users/loginForm.js"></script>
</form>
</div>
<div>
    <%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</div>
</body>

</html>
