<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>아이디 찾기</title>
    <link rel="stylesheet" type="text/css" href="/css/users/findId.css">

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/js/users/findId.js"></script>
</head>
<body>
<div class="page-contents">
<h2>아이디 찾기</h2>
<form id="findIdForm">
    <div>
        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" required />
    </div>
    <div>
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required />
    </div>
    <div>
        <button type="submit">아이디 찾기</button>
    </div>
</form>

<div id="result"></div>
<div>
    <a href="/login/login"><button>로그인 화면으로 돌아가기</button></a>
</div>
</div>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>