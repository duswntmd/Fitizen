<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false"%>
<c:set var="loginId" value="${pageContext.request.getSession(false)==null ? '' : pageContext.request.session.getAttribute('id')}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? '로그인' : 'ID='+=loginId}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png">
    <title>로그인</title>
    <style>
        @font-face {
            font-family: 'Pretendard-Bold';
            src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Bold.woff') format('woff');
            font-weight: 700;
            font-style: normal;
        }
        body, input, button {
            font-family: 'Pretendard-Bold';
        }
        h3 {
            font-family: 'Pretendard-Bold';
            font-size: 250%;
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
        form {
            position: absolute;
            padding: 20px;
            width: 300px;
            border-radius: 5px;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
    </style>
</head>
<body>
<div class="flex_container">
    <li id="logo"><a href="/"><img src="/image/logo.png" width="300" height="100"></a></li>
    <nav id="menu"><a href="/"><img src="/image/MenuBar.png" width="40" height="40"></a></nav>
</div>
<form action="<c:url value="/login/login"/>" method="post" onsubmit="return formCheck(this);">
    <h3 style="text-align:center">Login</h3>
    <div id="msg">
        <c:if test="${not empty param.msg}">
            <i class="fa fa-exclamation-circle"> ${URLDecoder.decode(param.msg)}</i>
        </c:if>
    </div>
    <input type="text" name="id" value="${cookie.id.value}" placeholder="아이디 입력" autofocus>
    <input type="password" name="pwd" placeholder="비밀번호">
    <input type="hidden" name="toURL" value="${param.toURL}">
    <p>
    <button>로그인</button>
    </p>
    <div>
        <p>
        <label><input type="checkbox" name="rememberId" value="on" ${empty cookie.id.value ? "":"checked"}> 아이디 기억</label>
        </p>
        <a href="/">아이디 찾기</a> |
        <a href="/">비밀번호 찾기</a>
    </div>
    <script>
        function formCheck(frm) {
            let msg ='';
            if(frm.id.value.length==0) {
                setMessage('id를 입력해주세요.', frm.id);
                return false;
            }
            if(frm.pwd.value.length==0) {
                setMessage('password를 입력해주세요.', frm.pwd);
                return false;
            }
            return true;
        }
        function setMessage(msg, element){
            document.getElementById("msg").innerHTML = ` ${'${msg}'}`;
            if(element) {
                element.select();
            }
        }
    </script>
</form>
</body>
</html>