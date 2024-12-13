<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>맞춤 운동 검사</title>
    <link rel="stylesheet" type="text/css" href="/css/findME/findMyExercise.css">

</head>
<body>

<div class="container">
    <h1>맞춤 운동 검사</h1>
    <form id="quizForm" onsubmit="return saveResultAndRedirect();">
        <!-- 질문들 -->
        <div class="question">
            <p>1. 운동을 얼마나 자주 하시나요?</p>
            <div class="options">
                <label><input type="radio" name="frequency" value="never" required>
                    <img class="exercise-img fade-in" src="image/never.jpg" alt="거의안함">
                    <span class="option-text">거의 안함</span>
                </label>
                <label><input type="radio" name="frequency" value="sometimes">
                    <img class="exercise-img fade-in" src="image/sometimes.jpg" alt="가끔 함">
                    <span class="option-text">가끔 함</span>
                </label>
                <label><input type="radio" name="frequency" value="regularly">
                    <img class="exercise-img fade-in" src="image/often.jpg" alt="자주 함">
                    <span class="option-text">자주 함</span>
                </label>
                <label><input type="radio" name="frequency" value="daily">
                    <img class="exercise-img fade-in" src="image/everyday.jpg" alt="거의 매일 함">
                    <span class="option-text">거의 매일함</span>
                </label>
            </div>
        </div>

        <!-- 두 번째 질문 -->
        <div class="question">
            <p>2. 선호하는 운동 유형은 무엇인가요?</p>
            <div class="options">
                <label><input type="radio" name="type" value="cardio" required>
                    <img class="exercise-img fade-in" src="image/cardio.jpg" alt="유산소 운동">
                    <span class="option-text">유산소 운동(ex.달리기, 자전거타기)</span></label>
                <label><input type="radio" name="type" value="strength">
                    <img class="exercise-img fade-in" src="image/health.jpg" alt="근력 운동">
                    <span class="option-text">근력 운동</span>
                </label>
                <label><input type="radio" name="type" value="flexibility">
                    <img class="exercise-img fade-in" src="image/yoga.jpg" alt="유연성 운동">
                    <span class="option-text">유연성 운동</span>
                </label>
                <label><input type="radio" name="type" value="balance">
                    <img class="exercise-img fade-in" src="image/pilates.jpg" alt="균형 운동">
                    <span class="option-text">균형 운동</span>
                </label>
            </div>
        </div>

        <!-- 세 번째 질문 -->
        <div class="question">
            <p>3. 운동을 통해 이루고 싶은 목표는 무엇인가요?</p>
            <div class="options">
                <label><input type="radio" name="goal" value="lose_weight" required>
                    <img class="exercise-img fade-in" src="image/weightloss.webp" alt="체중감량">
                    <span class="option-text">체중 감량</span>
                </label>
                <label><input type="radio" name="goal" value="build_muscle">
                    <img class="exercise-img fade-in" src="image/muscle.webp" alt="근육 증가">
                    <span class="option-text">근육 증가</span>
                </label>
                <label><input type="radio" name="goal" value="increase_endurance">
                    <img class="exercise-img fade-in" src="image/endurance.webp" alt="지구력 향상">
                    <span class="option-text">지구력 향상</span>
                </label>
                <label><input type="radio" name="goal" value="improve_flexibility">
                    <img class="exercise-img fade-in" src="image/flexibility.webp" alt="유연성 증가">
                    <span class="option-text">유연성 증가</span>
                </label>
            </div>
        </div>

        <!-- 네 번째 질문 -->
        <div class="question">
            <p>4. 운동에 사용할 수 있는 시간은 얼마나 되나요?</p>
            <div class="options">
                <label><input type="radio" name="time" value="short" required>
                    <img class="exercise-img fade-in" src="image/15min.jpg" alt="15~30분">
                    <span class="option-text">짧은시간(15~30분)</span>
                </label>
                <label><input type="radio" name="time" value="medium">
                    <img class="exercise-img fade-in" src="image/30min.jpg" alt="중간 시간(30~60분)">
                    <span class="option-text">중간 시간(30~60분)</span></label>
                <label><input type="radio" name="time" value="long">
                    <img class="exercise-img fade-in" src="image/60min.jpg" alt="긴 시간(60분 이상)">
                    <span class="option-text">긴 시간(60분 이상)</span></label>
            </div>
        </div>

        <!-- 다섯 번째 질문 -->
        <div class="question">
            <p>5. 운동을 할 때 주로 어떤 환경을 선호하나요?</p>
            <div class="options">
                <label><input type="radio" name="environment" value="indoor" required>
                    <img class="exercise-img fade-in" src="image/inside.webp" alt="실내">
                    <span class="option-text">실내</span>
                </label>
                <label><input type="radio" name="environment" value="outdoor">
                    <img class="exercise-img fade-in" src="image/outside.webp" alt="실외">
                    <span class="option-text">실외</span>
                </label>
                <label><input type="radio" name="environment" value="both">
                    <img class="exercise-img fade-in" src="image/both.webp" alt="실내,외 둘다">
                    <span class="option-text">둘 다</span>
                </label>
            </div>
        </div>

        <div class="question">
            <p>키(cm)와 몸무게(kg)를 입력해주세요:</p>
            <div class="options">
                <label>키: <input type="number" name="height" placeholder="cm" required></label>
                <label>몸무게: <input type="number" name="weight" placeholder="kg" required></label>
            </div>
        </div>

        <!-- <button type="submit">검사 결과로 이동</button> -->
        <button type="button" data-action="alternate" onclick="saveResultAndRedirectAlternate()">AI검사결과로 이동</button>
    </form>
</div>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/findME/findMyExercise.js"></script>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>
