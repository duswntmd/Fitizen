<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용자 정보 수정</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/css/users/updateUser.css">
</head>
<body>
<div id="page-wrap">
    <form id="updateUserForm">
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
            <label>비밀번호 (변경하려면 입력하세요):</label>
            <input type="password" class="form-control" name="pwd" placeholder="비밀번호 변경 시 입력" />
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

<script src="/js/users/updateUser.js"></script>
<%@ include file="footer.jsp" %>
</body>
</html>
