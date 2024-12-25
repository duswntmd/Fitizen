<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>운동 검사 결과</title>
    <link rel="stylesheet" type="text/css" href="/css/findME/findResult.css">

</head>
<body>
<div class="container">
    <h1>운동 검사 결과(추천 운동)</h1>
    <span id="resultText"></span>
    <button onclick="window.location.href='/findME'">다시 검사하기</button>
</div>

<script src="/js/findME/findResult.js"></script>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>
