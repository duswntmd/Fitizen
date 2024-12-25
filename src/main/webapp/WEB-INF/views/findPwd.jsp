<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<link rel="stylesheet" type="text/css" href="/css/users/findPwd.css">

<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/js/users/findPwd.js"></script>
</head>
<body>
<h2>비밀번호 찾기</h2>
<div class="page-contents">
<form id="findPwdForm">

    <div>
        <label for="id">아이디:</label>
        <input type="text" id="id" name="id" required />
    </div>
    <div>
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required />
    </div>
    <div>
        <button type="submit">임시 비밀번호 발급</button>
    </div>
        <div id="spinner" class="spinner"></div>

</form>

<div id="result"></div>
</div>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>
