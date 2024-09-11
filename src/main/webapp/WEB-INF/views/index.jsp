<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true"%> <!-- 세션 사용을 위해 session="true"로 변경 -->
<%-- "user" 객체가 세션에 있는지 확인하고, 로그인 상태 및 로그아웃 링크를 설정 --%>
<c:set var="user" value="${sessionScope.user}" />
<c:set var="loginId" value="${user == null ? '' : user.id}" />
<c:set var="loginOutLink" value="${user == null ? '/login/login' : '/login/logout'}" />

<%-- 문자열 결합을 명시적으로 처리 --%>
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
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png">
    <title>FITIZEN</title>
    <style>
        @font-face {
            font-family: 'Pretendard-Bold';
            src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Bold.woff') format('woff');
            font-weight: 700;
            font-style: normal;
        }
        @font-face {
            font-family: 'Pretendard-Black';
            src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Black.woff') format('woff');
            font-weight: 900;
            font-style: normal;
        }
        body {
            font-family: 'Pretendard-Bold';
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
        html, body {
            margin:0; height:100%; overflow:hidden;
        }

        .container {
            width: 100%;
            height: 200%;
            background: url("./image/Fitizen-Background.png");
        }

        .flex_container {
            display: flex;
            flex-direction: row;     /* 메인축(main axis) */
            flex-wrap: wrap;         /* 디폴트인 nowrap 대신 wrap 설정 */
            justify-content: space-around;
            align-content:space-around ;
            border:1px solid black;
            background-color: black;
            padding: 0.3em;
        }

        .nav {
            list-style: none;
            font-weight: bold;
            margin-bottom: 20px;
            float: right; /* Clear floats */
            width: 100%;
            /* Bring the nav above everything else--uncomment if needed.
            position: relative;
            z-index: 5;
            */
        }
        .nav li {
            float: left;
            margin-right: 0px;
            position: relative;
        }
        .nav a {
            display: block;
            padding: 5px;
            color: #fff;
            background-color: #000;
            text-decoration: none;
        }
        .nav a:hover {
            color: #fff;
            background-color: #000;
            text-decoration: underline;
        }

        /*--- DROPDOWN ---*/
        .nav ul {
            background-color: #fff; /* Adding a background makes the dropdown work properly in IE7+. Make this as close to your page's background as possible (i.e. white page == white background). */
            background: rgba(255,255,255,0); /* But! Let's make the background fully transparent where we can, we don't actually want to see it if we can help it... */
            list-style: none;
            position: absolute;
            left: -9999px; /* Hide off-screen when not needed (this is more accessible than display: none;) */
        }
        .nav ul li {
            padding-top: 1px; /* Introducing a padding between the li and the a give the illusion spaced items */
            float: none;
        }
        .nav ul a {
            white-space: nowrap; /* Stop text wrapping and creating multi-line dropdown items */
        }
        .nav li:hover ul { /* Display the dropdown on hover */
            left: 1px;
        }
        .nav li:hover a { /* These create persistent hover states, meaning the top-most link stays 'hovered' even when your cursor has moved down the list. */
            background-color: #000;
            text-decoration: underline;
        }
        .nav li:hover ul a { /* The persistent hover state does however create a global style for links even before they're hovered. Here we undo these effects. */
            text-decoration: none;
        }
        .nav li:hover ul li a:hover { /* Here we define the most explicit hover states--what happens when you hover each individual link. */
            background-color: #333;
        }
        @media screen and (min-width: 300px) {
            div > nav {
                padding: 24px 30px;
            }
        }
    </style>
</head>
<body>
<div class="container" />
<div class="flex_container">
<li id="logo"><a href="/"><img src="image/logo.png" width="300" height="100"></a></li>
<nav id="menu"><a href="/"><img src="image/MenuBar.png" width="38" height="38"></a></nav>
</div>
<ul class="nav">
<div class="flex_container">
    <li class="flex_item"><a href="/board/list">게시판</a></li>
    <li class="flex_item"><a href="${loginOutLink}">${loginOut}</a></li> <!-- 로그인/로그아웃 링크 -->
    <li class="flex_item"><a href="/register/add">회원가입</a></li>
    <li class="flex_item"><a href="/register/updateuser">수정</a></li>
    <li class="flex_item"><a href="/register/deleteuser">삭제</a></li>
    <li class="flex_item"><a href="/kakao/map">지도</a></li>
    <li class="flex_item"><a href="/challenge">챌린지</a></li>
</div>
<div style="text-align:center">
    <h1 style="line-height: 0.4; color:white">
        <p style="line-height: 0;">FITNESS + CITIZEN</p>
        <p style="line-height: 0; font-size: 150%;">FITIZEN</p>
    </h1>
    <h3 style="line-height: 50%; color:white">
        <p style="line-height: 0;">피트니스를 애용하는 헬린이들을 위한 소통창구</p>
        <p style="font-size: 135%;">피티즌 메인 페이지입니다.</p>
    </h3>
</div>

</li>
</body>
</html>
