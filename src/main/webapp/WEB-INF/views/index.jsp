<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FITIZEN</title>
    <link rel="stylesheet" type="text/css" href="/css/index/index.css">

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
                <h1 style="font-size: 400%; margin: 0; color: white">운동에 고민중이신가요</h1>
                <h1 style="font-size: 400%; margin: 0; color: white">FITIZEN 하나면</h1>
                <h1 style="font-size: 400%; margin: 0; color: white">운동 고민 끝</h1>
                <p style="font-size: 150%; margin-bottom: 0; color: white">헬스 홈트 운동 추천부터 분석까지</p>
                <p style="font-size: 150%; margin: 0; color: white">헬린이 초보자부터 트레이너까지</p>
                <p style="font-size: 150%; margin: 0; color: white">운동 고민은 이제 끝</p>
                <a href="/findME"><button style="margin-top: 60px; padding: 10px 20px; font-size: 50px; border-radius: 5px; background-color: #1BEDC7; color: white; border: none; cursor: pointer;">맞춤 운동 추천받기</button></a>
            </div>
            <div class="section-image">
                <img src="/image/Exercise recommendation.jpg" alt="운동 추천 이미지" style="width: 100%;">
            </div>
        </div>
    </section>

    <section class="section-2">
        <div class="section-content">
            <div class="section-image">
                <iframe width="770" height="440" src="https://www.youtube.com/embed/ahIXf1V4bhg?si=eQxePL4Dx32fi2yz" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
            </div>
            <div class="section-text">
                <h1 style="font-size: 250%; margin: 0; color: white">2024년 SKU FITIZEN 홍보영상</h1>
                <h1 style="font-size: 250%; margin: 0; color: white">잠깐보고 모든 기능을 100% 활용하세요</h1>
                <p style="font-size: 150%; margin-top: 10%; color: white">FITIZEN의 궁금한 질문! Q&A를 통해 바로 요청해주세요</p>
                <p style="font-size: 150%; margin: 0; color: white">신속하게 답변을 드리겠습니다</p>
                <a href="/qna"><button style="margin-top: 60px; padding: 10px 20px; font-size: 50px; border-radius: 5px; background-color: #1BEDC7; color: white; border: none; cursor: pointer;">Q&A 바로가기</button></a>
            </div>
        </div>
    </section>

    <section class="section-3">
        <div class="section-content">
            <div class="section-text">
                <h1 style="font-size: 240%; margin: 0; color: white">운동 자세에 고민중이신가요</h1>
                <h1 style="font-size: 240%; margin: 0; color: white">Ai 자세분석을 이용하여 피드백을 받으세요</h1>
                <h1 style="font-size: 240%; margin: 0; color: white">올바른 자세와 비교하여 결과를 보실 수 있습니다</h1>
                <p style="font-size: 150%; margin-bottom: 0; color: white">FITIZE은 자세의 동작을 하나도 놓치지 않습니다</p>
                <p style="font-size: 150%; margin: 0; color: white">사용자의 관절은 분석하여 문제점을 찾아 고쳐보세요</p>
                <p style="font-size: 150%; margin: 0; color: white">운동 자세의 고민은 이제 끝</p>
                <a href="/ai/userVideos"><button style="margin-top: 60px; padding: 10px 20px; font-size: 50px; border-radius: 5px; background-color: #1BEDC7; color: white; border: none; cursor: pointer;">Ai 자세분석받기</button></a>
            </div>
            <div class="section-image">
                <img src="/image/lstm.jpg" alt="자세분석 사진" style="width: 100%;">
            </div>
        </div>
    </section>

    <section class="section-4">
        <div class="section-content">
            <div class="section-image">
                <img src="/image/challenge.jpg" alt="경쟁 이미지" style="width: 100%;">
            </div>
            <div class="section-text">
                <h1 style="font-size: 250%; margin: 0; color: white">지금 경쟁에 뛰어들어 1등을 쟁취하세요</h1>
                <h1 style="font-size: 250%; margin: 0; color: white">FITIZEN이 당신과 함께합니다!</h1>
                <p style="font-size: 150%; margin-top: 10%; color: white">챌린지를 참여하여 전세계를 주무르고</p>
                <p style="font-size: 150%; margin: 0; color: white">가장 많이하는 챌린지와 선호하는 챌린지를 찾아보세요</p>
                <a href="/challenge"><button style="margin-top: 15%; padding: 10px 20px; font-size: 50px; border-radius: 5px; background-color: #1BEDC7; color: white; border: none; cursor: pointer;">챌린지 참여하기</button></a>
            </div>
        </div>
    </section>

    <!-- 카로셀 포함 섹션 -->
    <section class="section-5" style="height: calc(100vh - 250px); padding-top: 50px;">
        <div class="section-content">
            <div class="section-text">
                <h1 style="font-size: 240%; margin: 0; color: white">운동 자세에 고민중이신가요</h1>
                <h1 style="font-size: 240%; margin: 0; color: white">Ai 자세분석을 이용하여 피드백을 받으세요</h1>
                <h1 style="font-size: 240%; margin: 0; color: white">올바른 자세와 비교하여 결과를 보실 수 있습니다</h1>
                <p style="font-size: 150%; margin-bottom: 0; color: white">FITIZE은 자세의 동작을 하나도 놓치지 않습니다</p>
                <p style="font-size: 150%; margin: 0; color: white">사용자의 관절은 분석하여 문제점을 찾아 고쳐보세요</p>
                <p style="font-size: 150%; margin: 0; color: white">운동 자세의 고민은 이제 끝</p>
                <a href="/trainer"><button style="margin-top: 60px; padding: 10px 20px; font-size: 50px; border-radius: 5px; background-color: #1BEDC7; color: white; border: none; cursor: pointer;">트레이너 상담받기</button></a>
            </div>
            <div class="section-image">
                <img src="/image/trainer.jpg" alt="상담 이미지" style="width: 100%;">
            </div>
        </div>
<%--        <div class="extra" style="color: white; justify-content: center;">--%>
<%--            <%@ include file="carousel.jsp" %> <!-- 카로셀 파일 포함 -->--%>
<%--        </div>--%>
    </section>

    <%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->

    <script src="/js/index/index.js"></script>
</div>
</body>
</html>
