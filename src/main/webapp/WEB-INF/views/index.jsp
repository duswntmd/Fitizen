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

    <script src="/js/index/index.js"></script>
</div>


</body>
</html>
