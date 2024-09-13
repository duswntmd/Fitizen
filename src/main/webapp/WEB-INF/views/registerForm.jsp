<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false"%>
<c:set var="loginId" value="${pageContext.request.getSession(false)==null ? '' : pageContext.request.session.getAttribute('id')}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? '로그인' : 'ID='+=loginId}"/>
<%@ page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png"/>
    <title>회원가입</title>
    <style>
        @font-face {
            font-family: 'Pretendard-Bold';
            src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Bold.woff') format('woff');
            font-weight: 700;
            font-style: normal;
        }
        body {
            font-family: 'Pretendard-Bold';
        }
        html, body {
            margin:0; height:100%; overflow:hidden;
        }

        .flex_container {
            display: flex;
            flex-direction: row;     /* 메인축(main axis) */
            flex-wrap: wrap;         /* 디폴트인 nowrap 대신 wrap 설정 */
            justify-content: space-around;
            align-content:space-around ;
            border:1px solid black;
            background-color: black;
            padding: 0.1em;
        }

        .nav {
            list-style: none;
            font-weight: bold;
            margin-bottom: 10px;
            float: left; /* Clear floats */
            width: 100%;
            /* Bring the nav above everything else--uncomment if needed.
            position: relative;
            z-index: 5;
            */
        }
        .nav li {
            float: left;
            margin-right: 10px;
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
            background-color: #6b0c36;
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
            left: -40px; /* 리스트 안에 있는 하위 리스트는 40px 들여쓰기 되어 나타나므로 부모와 왼쪽을 정렬하기 위함 */
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
<div class="flex_container">
    <li id="logo"><a href="/"><img src="/image/logo.png" width="300" height="100"></a></li>
    <nav id="menu"><a href="/"><img src="/image/MenuBar.png" width="38" height="38"></a></nav>
</div>

<!-- Update the form tag to include id and onsubmit attribute -->
<form:form modelAttribute="user" id="registerForm" onsubmit="return submitForm(event);">
    <div class="title">Register</div>
    <div id="msg" class="msg"><form:errors path="id"/></div>
    <div pwd="msg" class="msg"><form:errors path="pwd"/></div>
    <span style="color:red;">${message}</span>

    <label for="">아이디</label>
    <input class="input-field" type="text" name="id" placeholder="4~10자리의 영대소문자와 숫자 조합">
    <label for="">비밀번호</label>
    <input class="input-field" type="password" name="pwd" placeholder="4~10자리의 영대소문자와 숫자 조합">
    <label for="">이름</label>
    <input class="input-field" type="text" name="name" placeholder="홍길동">
    <label for="">이메일</label>
    <input class="input-field" type="email" name="email" placeholder="example@gmail.co.kr">
    <label for="">생일</label>
    <input class="input-field" type="date" name="birth" placeholder="2020-12-31">

    <button type="submit">회원 가입</button>
</form:form>

<script>
    // Updated submitForm function to prevent default form submission and handle via AJAX
    function submitForm(event) {
        event.preventDefault(); // Prevent the default form submission

        const formData = new FormData(document.getElementById('registerForm'));

        fetch('/register/add', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    alert("성공적으로 회원가입 되었습니다.");  // Show success alert
                    window.location.href = '/login/login';  // Redirect on success
                } else {
                    document.getElementById('msg').textContent = data.message; // Show error message
                }
            })
        .catch(error => console.error('Error:', error));
    }
</script>
</body>
</html>