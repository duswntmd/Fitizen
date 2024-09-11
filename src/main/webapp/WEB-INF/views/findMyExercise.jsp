<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>맞춤 운동 검사</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
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
                <label><input type="radio" name="frequency" value="never" required> 거의 하지 않음</label>
                <label><input type="radio" name="frequency" value="sometimes"> 가끔 함</label>
                <label><input type="radio" name="frequency" value="regularly"> 정기적으로 함</label>
                <label><input type="radio" name="frequency" value="daily"> 매일 함</label>
            </div>
            <!-- 두 번째 질문 -->
            <div class="question">
                <p>2. 선호하는 운동 유형은 무엇인가요?</p>
                <div class="options">
                    <label><input type="radio" name="type" value="cardio" required> 유산소 운동 (예: 달리기, 자전거 타기)</label>
                    <label><input type="radio" name="type" value="strength"> 근력 운동 (예: 웨이트 트레이닝)</label>
                    <label><input type="radio" name="type" value="flexibility"> 유연성 운동 (예: 요가, 스트레칭)</label>
                    <label><input type="radio" name="type" value="balance"> 균형 운동 (예: 필라테스)</label>
                </div>
            </div>

            <!-- 세 번째 질문 -->
            <div class="question">
                <p>3. 운동을 통해 이루고 싶은 목표는 무엇인가요?</p>
                <div class="options">
                    <label><input type="radio" name="goal" value="lose_weight" required> 체중 감량</label>
                    <label><input type="radio" name="goal" value="build_muscle"> 근육 증가</label>
                    <label><input type="radio" name="goal" value="increase_endurance"> 지구력 향상</label>
                    <label><input type="radio" name="goal" value="improve_flexibility"> 유연성 개선</label>
                </div>
            </div>

            <!-- 네 번째 질문 -->
            <div class="question">
                <p>4. 운동에 사용할 수 있는 시간은 얼마나 되나요?</p>
                <div class="options">
                    <label><input type="radio" name="time" value="short" required> 짧은 시간 (15-30분)</label>
                    <label><input type="radio" name="time" value="medium"> 중간 시간 (30-60분)</label>
                    <label><input type="radio" name="time" value="long"> 긴 시간 (60분 이상)</label>
                </div>
            </div>

            <!-- 다섯 번째 질문 -->
            <div class="question">
                <p>5. 운동을 할 때 주로 어떤 환경을 선호하나요?</p>
                <div class="options">
                    <label><input type="radio" name="environment" value="indoor" required> 실내</label>
                    <label><input type="radio" name="environment" value="outdoor"> 실외</label>
                    <label><input type="radio" name="environment" value="both"> 실내 및 실외 모두</label>
                </div>
            </div>



            <!-- 제출 버튼 -->
            <button type="submit">결과 보기</button>
    </form>
</div>

<script>
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
        let resultText = "추천 운동: ";
        resultText += topThree.map((sport) => {
            switch (sport) {

                //링크 다 바꿔야됨

                case 'health': return "<a href='exerciseDetail?exercise=health'>헬스</a>";
                case 'yoga': return "<a href='exerciseDetail?exercise=yoga'>요가</a>";
                case 'pilates': return "<a href='exerciseDetail?exercise=pilates'>필라테스</a>";
                case 'cardio': return "<a href='exerciseDetail?exercise=cardio'>유산소 운동</a>";
                case 'swimming': return "<a href='exerciseDetail?exercise=swimming'>수영</a>";
                case 'basketball': return "<a href='exerciseDetail?exercise=basketball'>농구</a>";
                case 'tableTennis': return "<a href='exerciseDetail?exercise=tableTennis'>탁구</a>";
                case 'badminton': return "<a href='exerciseDetail?exercise=badminton'>배드민턴</a>";
                default: return "";
            }
        }).join(", ");

        // 세션 스토리지에 결과 저장
        sessionStorage.setItem('exerciseResult', resultText);

        // 결과 페이지로 이동
        window.location.href = '/findResult';
        return false; // 폼 제출 방지
    }
</script>

</body>
</html>
