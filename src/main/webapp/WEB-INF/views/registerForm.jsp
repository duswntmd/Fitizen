<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<%@ page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png"/>
    <title>회원가입</title>
    <style>
        /* 전체 페이지 레이아웃을 위한 설정 */
        html, body {
            height: 100%;
            margin: 0;
            display: flex;
            flex-direction: column;

        }

        /* 콘텐츠가 화면의 나머지 공간을 차지하도록 설정 */
        .page-contents {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: center; /* 콘텐츠가 가운데 정렬되도록 설정 */
            align-items: center;
        }

        .input-field {
            display: block;
            margin-bottom: 10px;
        }

        .title {
            font-size: 24px;
            margin-bottom: 20px;
        }

        button {
            display: block;
            margin-top: 20px;
        }
    </style>
</head>

<body>
<div class="page-contents">

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
</div>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
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