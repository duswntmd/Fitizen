<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI 검사 결과</title>
    <style>
        .exercise-container {
            display: flex;
            flex-direction: column;
            align-items: center; /* 가운데 정렬 */
            text-align: center;  /* 텍스트 가운데 정렬 */
            margin: 20px 0;      /* 상하 여백 */
        }
        .exercise-img {
            width: 500px;  /* 이미지 너비 조정 */
            height: auto;  /* 비율 유지 */
            margin-bottom: 10px; /* 이미지와 텍스트 사이 여백 */
        }
        .exercise-text {
            font-size: 20px; /* 텍스트 크기 조정 */
            margin-top: 10px; /* 이미지와 텍스트 사이 여백 */
        }
        .fade-in {
            opacity: 0;
            transition: opacity 1s ease-in;
        }

        .fade-in.show {
            opacity: 1;
        }
    </style>
</head>
<body>
<h1 style="text-align: center;">AI 추천 운동</h1>

<c:choose>
    <c:when test="${exercise == 'health'}">
        <div class="exercise-container">
            <a href='../exerciseDetail?exercise=health'><img class='exercise-img fade-in' src='/image/health.jpg' alt='헬스' /></a>
            <div class='exercise-text'>헬스(${confidence_score}%)</div>
        </div>
    </c:when>
    <c:when test="${exercise == 'yoga'}">
        <div class="exercise-container">
            <a href='../exerciseDetail?exercise=yoga'><img class='exercise-img fade-in' src='/image/yoga.jpg' alt='요가' /></a>
            <div class='exercise-text'>요가(${confidence_score}%)</div>
        </div>
    </c:when>
    <c:when test="${exercise == 'pilates'}">
        <div class="exercise-container">
            <a href='../exerciseDetail?exercise=pilates'><img class='exercise-img fade-in' src='/image/pilates.jpg' alt='필라테스' /></a>
            <div class='exercise-text'>필라테스(${confidence_score}%)</div>
        </div>
    </c:when>
    <c:when test="${exercise == 'cardio'}">
        <div class="exercise-container">
            <a href='../exerciseDetail?exercise=cardio'><img class='exercise-img fade-in' src='/image/cardio.jpg' alt='유산소 운동' /></a>
            <div class='exercise-text'>유산소 운동(${confidence_score}%)</div>
        </div>
    </c:when>
    <c:when test="${exercise == 'swimming'}">
        <div class="exercise-container">
            <a href='../exerciseDetail?exercise=swimming'><img class='exercise-img fade-in' src='/image/swimming.jpg' alt='수영' /></a>
            <div class='exercise-text'>수영(${confidence_score}%)</div>
        </div>
    </c:when>
    <c:when test="${exercise == 'basketball'}">
        <div class="exercise-container">
            <a href='../exerciseDetail?exercise=basketball'><img class='exercise-img fade-in' src='/image/basketball.jpg' alt='농구' /></a>
            <div class='exercise-text'>농구(${confidence_score}%)</div>
        </div>
    </c:when>
    <c:when test="${exercise == 'tableTennis'}">
        <div class="exercise-container">
            <a href='../exerciseDetail?exercise=tableTennis'><img class='exercise-img fade-in' src='/image/tabletennis.jpg' alt='탁구' /></a>
            <div class='exercise-text'>탁구(${confidence_score}%)</div>
        </div>
    </c:when>
    <c:when test="${exercise == 'badminton'}">
        <div class="exercise-container">
            <a href='../exerciseDetail?exercise=badminton'><img class='exercise-img fade-in' src='/image/badminton.jpg' alt='배드민턴' /></a>
            <div class='exercise-text'>배드민턴(${confidence_score}%)</div>
        </div>
    </c:when>
    <c:otherwise>
        <p style="text-align: center;">추천 운동 데이터가 없습니다.</p>
    </c:otherwise>
</c:choose>
<script>
    window.addEventListener('load', function() {
        // 모든 fade-in 클래스를 가진 요소를 선택합니다.
        const fadeInElements = document.querySelectorAll('.fade-in');

        // 각 요소에 show 클래스를 추가하여 페이드인 효과를 적용합니다.
        fadeInElements.forEach(function(element) {
            setTimeout(() => {
                element.classList.add('show');
            }, 500); // 원하는 딜레이를 설정할 수 있습니다 (500ms)
        });
    });
</script>

<%@ include file="footer.jsp" %>
</body>
</html>
