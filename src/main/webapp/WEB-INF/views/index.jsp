<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FITIZEN</title>
    <style>
        /* 기본 스타일 */
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            overflow-x: hidden;
            overflow-y: hidden;
            font-family: 'Pretendard-Black';
        }


        /* 섹션 스타일 */
        section {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }


        /* 섹션 내부 콘텐츠 정렬 */
        .section-content {
            display: flex;
            width: 80%;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
        }

        /* 텍스트와 이미지 스타일 */
        .section-text {
            flex: 1;
            color: #000;
            padding: 20px;

        }

        .section-image {
            flex: 1;
            text-align: center;
        }
        .header-container.hidden {
            opacity: 0;
            visibility: hidden;
            transition: opacity 0.3s ease;
        }

        .section-1 {
            /*background: url("../image/cardio.jpg") no-repeat center center;*/
            background: linear-gradient(to right, #ff7e5f, #feb47b); /* 오렌지에서 밝은 노랑으로 */
            background-size: cover;
            background-attachment: scroll;
        }

        .section-2 {
            /*background: url("../image/cycling.jpg") no-repeat center center;*/
            background: linear-gradient(to right, #00c9ff, #92fe9d); /* 밝은 블루에서 민트 그린으로 */
            background-size: cover;
            background-attachment: scroll;
        }

        .section-3 {
           /* background: url("../image/badminton.jpg") no-repeat center center;*/
            background: linear-gradient(to right, #F8A2C8 , #A2E6F0 ); /* 진한 퍼플에서 딥 핑크로 */
            background-size: cover;
            background-attachment: scroll;
        }
        .section-4 {
            /*background: url("../image/competing.jpg") no-repeat center center;*/
            background: linear-gradient(to right, #f12711, #f5af19); /* 딥 레드에서 밝은 옐로우로 */
            background-size: cover;
            background-attachment: scroll;
        }

        .section-5 {
            /*background: url("../image/fb.jpg") no-repeat center center;*/
            background: linear-gradient(to right, #FF6A95, #6A82FB);
            background-size: cover;
            background-attachment: scroll;
        }

        /* 반응형 스타일 */
        @media (max-width: 768px) {
            .section-content {
                flex-direction: column;
                text-align: center;
            }
        }
        .section-nav {
            position: fixed;
            top: 50%;
            right: 20px;
            transform: translateY(-50%);
            display: flex;
            flex-direction: column;
            gap: 10px;
            z-index: 100;
        }

        .nav-dot {
            width: 12px;
            height: 12px;
            background-color: lightgray;
            border-radius: 50%;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .nav-dot.active {
            background-color: #000; /* 활성화된 섹션의 색상 */
        }
    </style>
</head>
<body>
<div class="content-wrapper">


    <div class="section-nav">
        <div class="nav-dot" data-section="0"></div>
        <div class="nav-dot" data-section="1"></div>
        <div class="nav-dot" data-section="2"></div>
        <div class="nav-dot" data-section="3"></div>
        <div class="nav-dot" data-section="4"></div>
        <div class="nav-dot" data-section="5"></div>
    </div>

    <section class="section-0" style="height: fit-content">

        <%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
    </section>

    <!-- 메인 섹션들 -->
    <section class="section-1" style="height: calc(100vh);">
        <div class="section-content">
            <div class="section-text">
                <h1 style="font-size: 400%">FITIZEN</h1>
                <p style="font-size: 250%">초보자들을 위한 운동 추천 사이트</p>
            </div>
            <div class="section-image">
                <img src="/image/stretching.jpg" alt="운동 이미지" style="width: 100%;">
            </div>
        </div>
    </section>

    <section class="section-2">
        <div class="section-content">
            <div class="section-text">
                <h1 style="font-size: 400%">자신에게 딱 맞는 운동을 찾아보세요</h1>
                <p style="font-size: 200%">어떤 운동을 해야 할지 고민이신가요?</p>
                <p style="font-size: 200%">맞춤 운동 검사를 통해 내게 딱 맞는 운동을 확인해보세요</p>
            </div>
            <div class="section-image">
                <img src="/image/thinking.jpg" alt="고민 이미지" style="width: 100%;">
            </div>
        </div>
    </section>

    <section class="section-3">
        <div class="section-content">
            <div class="section-text">
                <h1 style="font-size: 400%">근처의 운동시설을 찾아보세요</h1>
                <p style="font-size: 200%">운동을 어디서 해야할지 고민이신가요?</p>
                <p style="font-size: 200%">근처에 있는 운동 시설을 찾아드립니다.</p>
            </div>
            <div class="section-image">
                <img src="/image/map.jpg" alt="지도 사진" style="width: 100%;">
            </div>
        </div>
    </section>

    <section class="section-4">
        <div class="section-content">
            <div class="section-text">
                <h1 style="font-size: 400%">다른 사용자들과 정보를 공유하고, 경쟁해보세요.</h1>
                <p style="font-size: 200%">게시판에서 정보를 공유하고, 챌린지에서 다른 사용자들과 경쟁해보세요.</p>
            </div>
            <div class="section-image">
                <img src="/image/competing.jpg" alt="경쟁 이미지" style="width: 100%;">
            </div>
        </div>
    </section>

    <!-- 카로셀 포함 섹션 -->
    <section class="section-5" style="height: calc(100vh - 250px); padding-top: 50px;">
        <div class="extra" style="color: white; justify-content: center;">
            <%@ include file="carousel.jsp" %> <!-- 카로셀 파일 포함 -->
        </div>
    </section>

    <%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->

    <script>
        let currentSection = 0;
        const sections = document.querySelectorAll('section');
        const navDots = document.querySelectorAll('.nav-dot');

        // 섹션 위치에 따라 헤더 표시 여부 업데이트
        function checkHeaderVisibility() {
            const section1Top = section1.getBoundingClientRect().top;
            const section1Bottom = section1.getBoundingClientRect().bottom;

            if (section1Top <= 0 && section1Bottom > 0) {
                header.classList.remove('hidden'); // 섹션 1에 있을 때 헤더 표시
            } else {
                header.classList.add('hidden'); // 다른 섹션에서는 헤더 숨김
            }
        }

        function updateNavDots() {
            navDots.forEach(dot => dot.classList.remove('active'));
            navDots[currentSection].classList.add('active');
        }

        // 스크롤 이벤트 처리 함수
        window.addEventListener('wheel', function(event) {
            if (event.deltaY > 0) { // 아래로 스크롤
                currentSection = Math.min(currentSection + 1, sections.length - 1);
            } else { // 위로 스크롤
                currentSection = Math.max(currentSection - 1, 0);
            }
            sections[currentSection].scrollIntoView({ behavior: 'smooth' });
            updateNavDots(); // 네비게이션 점 업데이트
        });

        // 점 네비게이션 클릭 이벤트
        navDots.forEach(dot => {
            dot.addEventListener('click', function() {
                currentSection = parseInt(dot.getAttribute('data-section'));
                sections[currentSection].scrollIntoView({ behavior: 'smooth' });
                updateNavDots();
            });
        });

        updateNavDots(); // 초기 점 업데이트




    </script>
</div>


</body>
</html>
