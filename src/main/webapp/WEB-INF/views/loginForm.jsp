<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false"%>
<c:set var="loginId" value="${pageContext.request.getSession(false)==null ? '' : pageContext.request.session.getAttribute('id')}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? '로그인' : 'ID='+=loginId}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .login-container {
            background: #fff;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }
        .login-container h3 {
            margin-bottom: 1.5rem;
        }
        .login-container .form-group {
            margin-bottom: 1rem;
        }
        .login-container .form-control {
            border-radius: 4px;
            padding: 0.75rem;
        }
        .login-container .btn {
            width: 100%;
            padding: 0.75rem;
        }
        .alert {
            margin-bottom: 1rem;
        }
        .login-container .form-check-label {
            margin-left: 0.5rem;
        }
    </style>
</head>
<body>
<div class="login-container">
    <form action="<c:url value='/login/login'/>" method="post" onsubmit="return formCheck(this);">
        <h3 id="title">Login</h3>
        <div id="msg" class="alert alert-danger" role="alert">
            <c:if test="${not empty param.msg}">
                <i class="fa fa-exclamation-circle"> ${URLDecoder.decode(param.msg)}</i>
            </c:if>
        </div>
        <div class="form-group">
            <input type="text" class="form-control" name="id" value="${cookie.id.value}" placeholder="아이디 입력" autofocus>
        </div>
        <div class="form-group">
            <input type="password" class="form-control" name="pwd" placeholder="비밀번호">
        </div>
        <input type="hidden" name="toURL" value="${param.toURL}">
        <button type="submit" class="btn btn-primary">로그인</button>
        <div class="mt-3">
            <div class="form-check">
                <input type="checkbox" class="form-check-input" name="rememberId" value="on" ${empty cookie.id.value ? '' : 'checked'}>
                <label class="form-check-label">아이디 기억</label>
            </div>
            <div class="mt-2">
                <a href="/login/findId">아이디 찾기</a> |
                <a href="/login/findPwd">비밀번호 찾기</a>
            </div>
        </div>
    </form>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function formCheck(frm) {
        let msg = '';
        if (frm.id.value.length === 0) {
            setMessage('아이디를 입력해주세요.', frm.id);
            return false;
        }
        if (frm.pwd.value.length === 0) {
            setMessage('비밀번호를 입력해주세요.', frm.pwd);
            return false;
        }
        return true;
    }

    function setMessage(msg, element) {
        document.getElementById("msg").innerHTML = `<i class="fa fa-exclamation-circle"></i> ${msg}`;
        if (element) {
            element.select();
        }
    }
</script>
</body>
</html>
