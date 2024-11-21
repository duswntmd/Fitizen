<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>운동법 설명</title>
    <link rel="stylesheet" type="text/css" href="/css/findME/exerciseDetail.css">

</head>
<body>

<div class="container">
    <h1 id="exerciseTitle">운동법</h1>
    <span id="exerciseDescription"></span>

    <div class="video-container">
        <iframe id="exerciseVideo" width="100%" height="315" src="" frameborder="0" allowfullscreen></iframe>
    </div>

    <button onclick="window.location.href='/findResult'">운동검사 결과로 돌아가기</button>
    <button onclick="findExerciseFacility()">운동시설 찾기</button>
</div>

<script src="/js/findME/exerciseDetail.js"></script>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>
