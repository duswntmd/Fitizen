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
    <style>
        .exercise-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
            margin: 20px 0;
        }
        .exercise-text {
            font-size: 20px;
            margin-top: 10px;
        }
    </style>
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
    <canvas id="exerciseChart" width="450" height="450"></canvas> <!-- 차트 크기 조정 -->
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

    // Chart.js를 이용하여 원형 차트 생성 및 클릭 이벤트 추가
    const ctx = document.getElementById('exerciseChart').getContext('2d');
    const exerciseChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: exercises,
            datasets: [{
                data: confidenceScores, // 각 운동의 정확도
                backgroundColor: ['#4CAF50', '#FF7043', '#29B6F6'], // 각 운동에 대한 색상
                borderWidth: 1
            }]
        },
        options: {
            responsive: false, // 반응형을 비활성화하여 고정된 크기 유지
            maintainAspectRatio: false, // 차트 비율을 고정하지 않음
            onClick: (event, elements) => { // 최상위에 위치
                if (elements.length > 0) {
                    const index = elements[0].index;
                    const label = exerciseChart.data.labels[index];
                    window.location.href = exerciseLinks[label];
                }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'bottom'
                },
                datalabels: {
                    color: '#ffffff', // 텍스트 색상
                    font: {
                        weight: 'bold',
                        size: 14
                    },
                    formatter: (value, context) => {
                        const exercise = context.chart?.data?.labels?.[context.dataIndex] || "Unknown Exercise";
                        return exercise+":"+ value+"%"; // 운동 이름과 % 값 표시
                    },
                    // 각 데이터 포인트별 높이 조정
                    offset: (context) => {
                        if (context.dataIndex === 1) { // 운동2
                            return 10; // 기본 위치에서 약간 위로 이동
                        } else if (context.dataIndex === 2) { // 운동3
                            return 40; // 기본 위치에서 약간 아래로 이동
                        }
                        return 0; // 기본 위치
                    },
                    anchor: 'end', // 데이터 레이블의 위치 설정
                    align: 'start' // 데이터 레이블 정렬
                }
            }
        },
        plugins: [ChartDataLabels] // datalabels 플러그인 활성화
    });
</script>

<%@ include file="footer.jsp" %>
</body>
</html>
