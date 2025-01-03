<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI 추천 운동</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels"></script> <!-- 데이터 레이블 플러그인 추가 -->
    <link rel="stylesheet" type="text/css" href="/css/findME/aiResult.css">

</head>
<body>
<h1 style="text-align: center;">AI 추천 운동</h1>

<%-- 세션에서 추천 운동과 정확도 가져오기 --%>
<c:set var="exercise1" value="${sessionScope.exercise1}" />
<c:set var="confidence_score1" value="${sessionScope.confidence_score1 != null ? sessionScope.confidence_score1  : 0}" />
<c:set var="exercise2" value="${sessionScope.exercise2}" />
<c:set var="confidence_score2" value="${sessionScope.confidence_score2 != null ? sessionScope.confidence_score2  : 0}" />
<c:set var="exercise3" value="${sessionScope.exercise3}" />
<c:set var="confidence_score3" value="${sessionScope.confidence_score3 != null ? sessionScope.confidence_score3 : 0}" />

<div class="exercise-container">
    <div class="exercise-text">추천 운동과 정확도 비교(차트를 클릭해 운동 상세정보로 이동)</div>
    <canvas id="exerciseChart" width="500" height="500"></canvas> <!-- 차트 크기 조정 -->
</div>

<script>
    // 운동에 대한 링크 설정
    const exerciseLinks = {
        '${exercise1}': '/exerciseDetail?exercise=${exercise1}',
        '${exercise2}': '/exerciseDetail?exercise=${exercise2}',
        '${exercise3}': '/exerciseDetail?exercise=${exercise3}'
    };

    // 각 추천 운동과 정확도 데이터
    const exercises = ['${exercise1}', '${exercise2}', '${exercise3}'];

    const confidenceScores = [
        parseFloat("${confidence_score1}"),
        parseFloat("${confidence_score2}"),
        parseFloat("${confidence_score3}")
    ];

    console.log("Confidence Scores:", confidenceScores); // 확인용
    console.log('${exercise1}', '${confidence_score1}', '${exercise2}', '${confidence_score2}', '${exercise3}', '${confidence_score3}');
</script>
<script src="/js/findME/aiResult.js"defer></script>

<%@ include file="footer.jsp" %>
</body>
</html>
