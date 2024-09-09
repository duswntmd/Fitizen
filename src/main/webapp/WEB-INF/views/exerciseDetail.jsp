<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>운동법 설명</title>
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
            text-align: center;
        }

        h1 {
            color: #333;
        }

        p {
            font-size: 18px;
            color: #555;
        }

        .video-container {
            margin: 20px 0;
        }

        button {
            display: block;
            width: 100%;
            padding: 12px;
            font-size: 18px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 20px;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="container">
    <h1 id="exerciseTitle">운동법</h1>
    <p id="exerciseDescription"></p>

    <div class="video-container">
        <iframe id="exerciseVideo" width="100%" height="315" src="" frameborder="0" allowfullscreen></iframe>
    </div>

    <button onclick="window.history.back()">이전 페이지로 돌아가기</button>
</div>

<script>
    // URL에서 운동 종목을 가져옴
    const urlParams = new URLSearchParams(window.location.search);
    const exercise = urlParams.get('exercise');

    // 운동 설명과 영상 URL 매핑
    const exerciseDetails = {
        health: {
            title: "헬스",
            description: "헬스는 체력을 강화하고 근육을 키우기 위한 운동입니다. 주로 웨이트 트레이닝을 포함합니다.",
            videoUrl: "https://www.youtube.com/embed/example1" // 예시 URL
        },
        yoga: {
            title: "요가",
            description: "요가는 몸의 유연성을 증가시키고 마음의 안정을 찾는 운동입니다.",
            videoUrl: "https://www.youtube.com/embed/example2" // 예시 URL
        },
        pilates: {
            title: "필라테스",
            description: "필라테스는 근력 강화와 유연성, 균형을 중점적으로 향상시키는 운동입니다.",
            videoUrl: "https://www.youtube.com/embed/example3" // 예시 URL
        },
        cardio: {
            title: "유산소 운동",
            description: "유산소 운동은 심혈관 계통을 강화하고 지구력을 향상시키는 운동입니다.",
            videoUrl: "https://www.youtube.com/embed/example4" // 예시 URL
        },
        swimming: {
            title: "수영",
            description: "수영은 전신 운동으로, 체력을 강화하고 심폐 기능을 향상시킵니다.",
            videoUrl: "https://www.youtube.com/embed/bPWqO20Xzco?si=_mOqsK9RCT6KBs46&amp;controls=0" // 예시 URL
        },
        basketball: {
            title: "농구",
            description: "농구는 팀 운동으로, 민첩성과 협동심을 기르는데 좋은 운동입니다.",
            videoUrl: "https://www.youtube.com/embed/example6" // 예시 URL
        },
        tableTennis: {
            title: "탁구",
            description: "탁구는 실내 스포츠로, 빠른 반사신경과 손눈 협응을 기르는데 좋습니다.",
            videoUrl: "https://www.youtube.com/embed/example7" // 예시 URL
        },
        badminton: {
            title: "배드민턴",
            description: "배드민턴은 실내외에서 즐길 수 있는 운동으로, 민첩성과 반사신경을 향상시킵니다.",
            videoUrl: "https://www.youtube.com/embed/example8" // 예시 URL
        }
    };

    if (exercise && exerciseDetails[exercise]) {
        const details = exerciseDetails[exercise];
        document.getElementById('exerciseTitle').innerText = details.title;
        document.getElementById('exerciseDescription').innerText = details.description;
        document.getElementById('exerciseVideo').src = details.videoUrl;
    } else {
        document.getElementById('exerciseTitle').innerText = "운동 정보를 찾을 수 없습니다.";
        document.getElementById('exerciseDescription').innerText = "잘못된 운동 종목이 선택되었습니다.";
    }
</script>

</body>
</html>
