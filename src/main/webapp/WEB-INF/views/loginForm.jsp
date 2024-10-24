<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->

<!DOCTYPE html>

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        @font-face {
            font-family: 'Pretendard-Bold';
            src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Bold.woff') format('woff');
            font-weight: 700;
            font-style: normal;
        }
         input, button {
            font-family: 'Pretendard-Bold';
        }
        .page-contents {
            display: flex;
            flex-direction: column; /* 위에서 아래로 레이아웃을 설정 */
            flex: 1; /* 컨텐츠 영역이 남은 공간을 차지하도록 설정 */
            justify-content: center; /* 수직 중앙 정렬 */
            align-items: center; /* 수평 중앙 정렬 */
            min-height: calc(100vh - 100px); /* 푸터 높이 조정 필요 */
        }
        h3 {
            font-family: 'Pretendard-Bold';
            font-size: 250%;
        }
        html {
            margin:0;
            height:100%;
            overflow:hidden;
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
<div class="page-contents">
<form action="<c:url value="/dologin"/>" method="post" onsubmit="return formCheck(this);">

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
        <a href="/mail/findId">아이디 찾기</a> |
        <a href="/mail/findPwd">비밀번호 찾기</a>
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
</div>
<div>
    <%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</div>
</body>

</html>
