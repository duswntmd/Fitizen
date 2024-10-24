<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true"%>

<c:set var="user" value="${sessionScope.user}" />
<c:set var="loginId" value="${user == null ? '' : user.id}" />
<c:set var="loginOutLink" value="${user == null ? '/login/login' : '/login/logout'}" />

<c:choose>
    <c:when test="${user == null}">
        <c:set var="loginOut" value="Login" />
    </c:when>
    <c:otherwise>
        <c:set var="loginOut" value="${'Logout ('}${loginId}${')'}" />
    </c:otherwise>
</c:choose>




<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FITIZEN</title>



</head>
<body>
<div class="content-wrapper">
    <!-- 페이지 컨텐츠 -->
    <div style="text-align:center">
        <section>
            <%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
        <h1 style="line-height: 0.4; color:Black">
            <p style="line-height: 0; font-size: 70%;">초보자들을 위한 운동 추천 사이트</p>
            <p style="line-height: 0; font-size: 150%;">FITIZEN</p>
        </section>
        </h1>
        <section>
        <h4>
           <p style="line-height: 100%; font-size: 90%;"> 자신에게 딱 맞는 운동을 찾아보세요</p>
        </h4>
        </section>
        <section>
        <h4>
            <p style=" line-height: 100%;"> 근처의 운동시설을 찾아보세요</p>
        </h4>
        </section>
        <section>
        <h4>
            <p style=" line-height: 100%;"> 다른 사용자들과 정보를 공유하고, 경쟁해보세요.</p>
        </h4>
        </section>
    </div>
    <section style="margin-top: 100px; margin-bottom: 0;">
    <div class="extra" style="color: white;">

        <%@ include file="carousel.jsp" %>
</div>


    </section>
    <footer>
        <%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
    </footer>


    <script>
        let currentSection = 0;
        const sections = document.querySelectorAll('section');

        // 스크롤 이벤트 처리 함수
        window.addEventListener('wheel', function(event) {
            if (event.deltaY > 0) { // 아래로 스크롤
                currentSection = Math.min(currentSection + 1, sections.length - 1);
            } else { // 위로 스크롤
                currentSection = Math.max(currentSection - 1, 0);
            }
            sections[currentSection].scrollIntoView({ behavior: 'smooth' });
        });
    </script>
</div>
</body>

<style>
 html {
        height: auto;
        margin: 0; /* 기본 여백 제거 */
        overflow-x: hidden; /* 가로 스크롤 방지 */
     display: flex;
     flex-direction: column;
     scroll-snap-type: y mandatory; /* 세로 스크롤에 맞춰 스냅 */
    overflow-y: hidden;
    }
 body {
     justify-content: space-between; /* 푸터를 하단에 배치 */
     min-height: 100vh; /* 화면 전체 높이를 채움 */
 }
 section {
     height: 100vh; /* 각 섹션을 한 화면 크기로 설정 */
 }

    .content-wrapper {
        flex-grow: 1;
        width: 100%;
        background: url("../image/Fitizen_Background.jpg") no-repeat center center;
        background-size: cover; /* 배경 이미지를 화면에 맞춤 */
    }

    body {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        min-height: 100vh;
    }
    h1 {
        font-family: 'Pretendard-Black';
        font-size : 400%;

    }
    h2 {
        font-family: 'Pretendard-Black';
        font-size : 600%;
    }
    h3 {
        font-family: 'Pretendard-Black';
        font-size : 200%;
    }
    h4 {
        font-family: 'Pretendard-Black';
        font-size : 300%;
    }
 .extra {
     padding-bottom: 200px; /* 푸터 높이만큼 패딩 추가 */
 }

</style>
</html>
