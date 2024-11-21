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
    <link rel="stylesheet" type="text/css" href="/resources/static/css/indexStyle.css">
    <title>회원가입</title>
    <link rel="stylesheet" type="text/css" href="/css/users/registerForm.css">

</head>

<body>
<div class="page-contents">
    <div class="form-container">
    <form:form modelAttribute="user" id="registerForm" onsubmit="return submitForm(event);">
        <div class="title">회원가입</div>
        <div id="msg" class="msg"><form:errors path="id"/></div>
        <div pwd="msg" class="msg"><form:errors path="pwd"/></div>
        <span style="color:red;">${message}</span>

        <label for="">아이디</label>
        <input class="input-field" type="text" name="id" placeholder="4~10자리의 영대소문자와 숫자 조합" required>

        <label for="">비밀번호</label>
        <input class="input-field" type="password" name="pwd" placeholder="4~10자리의 영대소문자와 숫자 조합" required>

        <label for="">이름</label>
        <input class="input-field" type="text" name="name" placeholder="홍길동" required>

        <label for="">이메일</label>
        <input class="input-field" type="email" name="email" placeholder="example@gmail.co.kr" required>

        <label for="">생일</label>
        <input class="input-field" type="date" name="birth" required>

        <label>회원 유형 선택</label>
        <select class="input-field" id="userType" onchange="toggleTrainerFields()">
            <option value="USER">일반 회원</option>
            <option value="TRAINER">트레이너</option>
        </select>

        <!-- 트레이너 정보 입력 필드 -->
        <div id="trainerFields" style="display: none;">
            <label for="">근무지</label>
            <input class="input-field" type="text" name="workPlaceName" placeholder="근무지 이름">

            <div class="col-sm-10">
                <label>주소</label>
                <input type="number" id="zipCode" name="zipCode" maxlength="10" placeholder="우편번호" style="width: 50%; display: inline;">
                <input type="button" id="zipp_btn" onclick="execDaumPostcode()" value="우편번호 찾기"><br>
                <input type="text" name="location" id="UserAdd1" maxlength="40" placeholder="기본 주소를 입력하세요" >
                <input type="text" name="locationDetail" id="UserAdd2" maxlength="40" placeholder="상세 주소를 입력하세요">
            </div>

            <label for="">경력 연수</label>
            <input class="input-field" type="number" name="workYears" placeholder="경력 연수 (년)">
        </div>

        <button type="submit">회원 가입</button>
    </form:form>
    </div>
</div>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->

<script src="/js/users/registerForm.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</body>
</html>
