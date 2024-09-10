<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true"%>
<c:set var="loginId" value="${pageContext.request.getSession(false)==null ? '' : pageContext.request.session.getAttribute('id')}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? '로그인' : 'ID='+=loginId}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용자 정보 수정</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f7f7f7;
        }
        #page-wrap {
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #333;
            margin-bottom: 30px;
            text-align: center;
        }
        label {
            margin-top: 10px;
            font-weight: bold;
            color: #555;
        }
        input[type="submit"] {
            margin-top: 20px;
            width: 100%;
            background-color: #007bff;
            color: #fff;
            padding: 10px;
            border: none;
            border-radius: 4px;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div id="page-wrap">
    <form action="${pageContext.request.contextPath}/register/updateuser" method="post">
        <h2>사용자 정보 수정</h2>
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <input type="hidden" name="id" value="${user.id}" />
        <div class="mb-3">
            <label>ID:</label>
            <span class="form-control-plaintext">${user.id}</span>
        </div>
        <div class="mb-3">
            <label>비밀번호:</label>
            <input type="password" class="form-control" name="pwd" value="${user.pwd}" />
        </div>
        <div class="mb-3">
            <label>이름:</label>
            <input type="text" class="form-control" name="name" value="${user.name}" />
        </div>
        <div class="mb-3">
            <label>이메일:</label>
            <input type="email" class="form-control" name="email" value="${user.email}" />
        </div>
        <div class="mb-3">
            <label>생년월일:</label>
            <input type="date" class="form-control" name="birth" value="${user.birth}" />
        </div>
        <input type="submit" class="btn btn-primary" value="수정하기">
    </form>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    let msg = "${msg}";
    if(msg=="WRT_OK") {
        alert("회원 정보가 성공적으로 수정되었습니다.");
        window.location.href ="/";
    }
</script>
</body>
</html>
