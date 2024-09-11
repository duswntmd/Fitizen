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
            description: "근육을 강화하고 몸을 탄탄하게 만들어주는 운동으로, 자신감 넘치는 몸매를 얻는 데 도움이 됩니다.",
            videoUrl: "https://www.youtube.com/embed/xzeE7pdI3bg?si=5yiVcIDHp47jirsA" // 예시 URL
        },
        yoga: {
            title: "요가",
            description: "유연성과 마음의 평화를 동시에 얻을 수 있는 운동으로, 몸과 마음의 힐링을 경험해보세요.",
            videoUrl: "https://www.youtube.com/embed/OBTl49bVk94?si=DkMVaSGw1GugrNmb" // 예시 URL
        },
        pilates: {
            title: "필라테스",
            description: "코어 근육을 단단하게 하고 자세를 교정해, 강하고 균형 잡힌 몸을 만들어줍니다.",
            videoUrl: "https://www.youtube.com/embed/sb51gF18cYo?si=h84tk_-iZ2p15Dmr" // 예시 URL
        },
        cardio: {
            title: "유산소 운동",
            description: "심장이 튼튼해지고 에너지가 넘치는 기분을 느끼며 체중도 관리할 수 있는 최고의 선택입니다.",
            videoUrl: "https://www.youtube.com/embed/OoytN1a8Klc?si=pJzJgXH5FzIP9rgZ" // 예시 URL
        },
        swimming: {
            title: "수영",
            description: "전신을 사용해 효과적으로 칼로리를 소모하고 근육을 강화하는 운동으로, 물속에서의 자유를 만끽하세요.",
            videoUrl: "https://www.youtube.com/embed/bPWqO20Xzco?si=_mOqsK9RCT6KBs46&amp;controls=0" // 예시 URL
        },
        basketball: {
            title: "농구",
            description: "친구들과 함께 활기차게 뛰고 공을 던지며 재미와 운동을 동시에 즐길 수 있습니다.",
            videoUrl: "https://www.youtube.com/embed/uqtHD41RldE?si=ZIaC-5zwh4QYrBEi"  // 예시 URL
        },
        tableTennis: {
            title: "탁구",
            description: "빠른 반사 신경과 집중력을 키우며, 즐겁게 운동할 수 있는 재미있는 스포츠입니다.",
            videoUrl: "https://www.youtube.com/embed/bvhH1NIZGA4?si=I8aqJL_0PAmyqZ2T" // 예시 URL
        },
        badminton: {
            title: "배드민턴",
            description: "빠른 속도와 기술을 요구하는 운동으로, 신나는 게임을 통해 체력과 민첩성을 기를 수 있습니다.",
            videoUrl: "https://www.youtube.com/embed/JZOeAeIkrgY?si=gBmTj_yT6NVEhqdG" // 예시 URL
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
