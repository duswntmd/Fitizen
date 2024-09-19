<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>맞춤 운동 검사</title>
    <style>
        .exercise-img {
            width: 300px;   /* 원하는 너비로 설정 */
            height: auto;   /* 비율에 맞게 높이 자동 조정 */
        }


        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .options input[type="radio"] {
            display: none; /* 숨기기 */
        }
        .options input[type="radio"]:checked + img {
            border: 3px solid #007bff; /* 파란색 테두리 */
            opacity: 0.6; /* 투명도 */
        }
        .container {
            max-width: 700px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .question {
            margin: 20px 0;
        }

        .question p {
            font-size: 18px;
            color: #555;
        }

        .options {
            margin: 10px 0;
        }


        .options label {
            display: block;
            font-size: 16px;
            margin-bottom: 8px;
            color: #333;
        }

        button {
            display: block;
            width: 100%;
            padding: 12px;
            font-size: 18px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 20px;
        }

        button:hover {
            background-color: #218838;
        }
    </style>
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
                    <img class="exercise-img fade-in" src="image/hardlyever.jpg" alt="거의안함">
                    <span class="option-text">거의 안함</span>
                </label>
                <label><input type="radio" name="frequency" value="sometimes">
                    <img class="exercise-img fade-in" src="image/sometimes.png" alt="가끔 함">
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
                    <img class="exercise-img fade-in" src="image/loseweight.jpg" alt="체중감량">
                    <span class="option-text">체중 감량</span>
                </label>
                <label><input type="radio" name="goal" value="build_muscle">
                    <img class="exercise-img fade-in" src="image/muscle.jpg" alt="근육 증가">
                    <span class="option-text">근육 증가</span>
                </label>
                <label><input type="radio" name="goal" value="increase_endurance">
                    <img class="exercise-img fade-in" src="image/endurance.jpg" alt="지구력 향상">
                    <span class="option-text">지구력 향상</span>
                </label>
                <label><input type="radio" name="goal" value="improve_flexibility">
                    <img class="exercise-img fade-in" src="image/flexibility.jpg" alt="유연성 증가">
                    <span class="option-text">유연성 증가</span>
                </label>
            </div>
        </div>

        <!-- 네 번째 질문 -->
        <div class="question">
            <p>4. 운동에 사용할 수 있는 시간은 얼마나 되나요?</p>
            <div class="options">
                <label><input type="radio" name="time" value="short" required>
                    <img class="exercise-img fade-in" src="image/short.jpg" alt="15~30분">
                    <span class="option-text">짧은시간(15~30분)</span>
                </label>
                <label><input type="radio" name="time" value="medium">
                    <img class="exercise-img fade-in" src="image/medium.jpg" alt="중간 시간(30~60분)">
                    <span class="option-text">중간 시간(30~60분)</span></label>
                <label><input type="radio" name="time" value="long">
                    <img class="exercise-img fade-in" src="image/long.jpg" alt="긴 시간(60분 이상)">
                    <span class="option-text">긴 시간(60분 이상)</span></label>
            </div>
        </div>

        <!-- 다섯 번째 질문 -->
        <div class="question">
            <p>5. 운동을 할 때 주로 어떤 환경을 선호하나요?</p>
            <div class="options">
                <label><input type="radio" name="environment" value="indoor" required>
                    <img class="exercise-img fade-in" src="image/inside.jpg" alt="실내">
                    <span class="option-text">실내</span>
                </label>
                <label><input type="radio" name="environment" value="outdoor">
                    <img class="exercise-img fade-in" src="image/outside.jpg" alt="실외">
                    <span class="option-text">실외</span>
                </label>
                <label><input type="radio" name="environment" value="both">
                    <img class="exercise-img fade-in" src="image/both.png" alt="실내,외 둘다">
                    <span class="option-text">둘 다</span>
                </label>
            </div>
        </div>

        <button type="submit">제출</button>
    </form>
</div>

<script>
    document.querySelectorAll('.exercise-img').forEach(img => {
        img.addEventListener('click', (event) => {
            const img = event.target;
            const questionGroup = img.classList[1]; // 'frequency-img'와 같은 클래스를 가져옵니다

            // 해당 질문의 이미지에서 'selected' 클래스 제거
            document.querySelectorAll(`.${questionGroup}`).forEach(image => {
                image.classList.remove('selected');
            });
            // 클릭된 이미지에 'selected' 클래스 추가
            img.classList.add('selected');

            // 해당 질문의 라디오 버튼 체크
            const associatedRadio = img.parentElement.querySelector('input[type="radio"]');
            if (associatedRadio) {
                associatedRadio.checked = true;
            }
        });
    });

    function saveResultAndRedirect() {
        // 사용자가 선택한 답변을 가져오기
        const form = document.getElementById('quizForm');
        const frequency = form.elements['frequency'].value;
        const type = form.elements['type'].value;
        const goal = form.elements['goal'].value;
        const time = form.elements['time'].value;
        const environment = form.elements['environment'].value;

        // 초기 점수 설정
        const scores = {
            health: 0,
            yoga: 0,
            pilates: 0,
            cardio: 0,
            swimming: 0,
            basketball: 0,
            tableTennis: 0,
            badminton: 0
        };

        // 빈도에 따른 점수 계산
        if (frequency === 'never') {
            scores.yoga += 2;
            scores.pilates += 2;
        } else if (frequency === 'sometimes') {
            scores.cardio += 2;
            scores.badminton += 1;
            scores.tableTennis += 1;
        } else if (frequency === 'regularly') {
            scores.health += 2;
            scores.swimming += 2;
            scores.basketball += 1;
        } else if (frequency === 'daily') {
            scores.health += 3;
            scores.cardio += 2;
            scores.swimming += 2;
            scores.basketball += 2;
        }

        // 선호 운동 유형에 따른 점수 계산
        if (type === 'cardio') {
            scores.cardio += 3;
            scores.swimming += 2;
            scores.basketball += 2;
        } else if (type === 'strength') {
            scores.health += 3;
            scores.badminton += 1;
            scores.tableTennis += 1;
        } else if (type === 'flexibility') {
            scores.yoga += 3;
            scores.pilates += 3;
        } else if (type === 'balance') {
            scores.pilates += 3;
            scores.yoga += 2;
            scores.badminton += 1;
        }

        // 목표에 따른 점수 계산
        if (goal === 'lose_weight') {
            scores.cardio += 2;
            scores.swimming += 2;
            scores.basketball += 1;
        } else if (goal === 'build_muscle') {
            scores.health += 3;
        } else if (goal === 'increase_endurance') {
            scores.cardio += 2;
            scores.swimming += 2;
            scores.basketball += 1;
        } else if (goal === 'improve_flexibility') {
            scores.yoga += 3;
            scores.pilates += 3;
        }

        // 운동 시간에 따른 점수 조정
        if (time === 'short') {
            scores.cardio += 1;
            scores.tableTennis += 1;
        } else if (time === 'medium') {
            scores.yoga += 1;
            scores.pilates += 1;
            scores.basketball += 1;
        } else if (time === 'long') {
            scores.health += 2;
            scores.swimming += 2;
            scores.cardio += 2;
        }

        // 환경에 따른 점수 조정
        if (environment === 'indoor') {
            scores.health += 2;
            scores.yoga += 2;
            scores.pilates += 2;
            scores.tableTennis += 1;
            scores.badminton += 1;
        } else if (environment === 'outdoor') {
            scores.cardio += 2;
            scores.swimming += 2;
            scores.basketball += 2;
        } else if (environment === 'both') {
            scores.cardio += 1;
            scores.yoga += 1;
            scores.pilates += 1;
            scores.health += 1;
            scores.swimming += 1;
            scores.basketball += 1;
            scores.tableTennis += 1;
            scores.badminton += 1;
        }

        // 추천 운동 결정
        const sortedScores = Object.entries(scores).sort(([,a], [,b]) => b - a);
        const topThree = sortedScores.slice(0, 3).map(([key]) => key);

        // 결과 텍스트 생성
        let resultText = "";
        resultText += topThree.map((sport) => {
            switch (sport) {

                case 'health': return "<a href='exerciseDetail?exercise=health'><img class='exercise-img fade-in' src='/image/health.jpg' alt='헬스'  /></a>" +
                    "<div class='exercise-text'>헬스</div>";
                case 'yoga': return "<a href='exerciseDetail?exercise=yoga'><img class='exercise-img fade-in' src='/image/yoga.jpg' alt='요가' /></a>" +
                    "     <div class='exercise-text'>요가</div>";
                case 'pilates': return "<a href='exerciseDetail?exercise=pilates'><img class='exercise-img fade-in' src='/image/pilates.jpg' alt='필라테스' /></a>" +
                    "     <div class='exercise-text'>필라테스</div>";
                case 'cardio': return "<a href='exerciseDetail?exercise=cardio'><img class='exercise-img fade-in' src='/image/cardio.jpg' alt='유산소 운동' /></a>" +
                    "     <div class='exercise-text'>유산소 운동</div>";
                case 'swimming': return "<a href='exerciseDetail?exercise=swimming'><img class='exercise-img fade-in' src='/image/swimming.jpg' alt='수영' /></a>" +
                    "     <div class='exercise-text'>수영</div>";
                case 'basketball': return "<a href='exerciseDetail?exercise=basketball'><img class='exercise-img fade-in' src='/image/basketball.jpg' alt='농구' /></a>" +
                    "     <div class='exercise-text'>농구</div>";
                case 'tableTennis': return "<a href='exerciseDetail?exercise=tableTennis'><img class='exercise-img fade-in' src='/image/tabletennis.jpg' alt='탁구' /></a>" +
                    "     <div class='exercise-text'>탁구</div>";
                case 'badminton': return "<a href='exerciseDetail?exercise=badminton'><img class='exercise-img fade-in' src='/image/badminton.jpg' alt='배드민턴' /></a>" +
                    "     <div class='exercise-text'>배드민턴</div>";
                default: return "";
            }
        }).join(" ");

        // 세션 스토리지에 결과 저장
        sessionStorage.setItem('exerciseResult', resultText);

        // 결과 페이지로 이동
        window.location.href = '/findResult';
        return false; // 폼 제출 방지
    }
</script>

</body>
</html>
