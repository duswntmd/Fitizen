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
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->

<script>
    function submitForm(event) {
        event.preventDefault(); // Prevent the default form submission

        const formData = new FormData(document.getElementById('registerForm'));

        const userType = document.getElementById('userType').value;
        formData.append('is_trainer', userType === 'TRAINER' ? 'Y' : 'N');

        if (userType === 'USER') {
            // 일반 유저일 경우 location과 locationDetail 필드를 제거
            const userAdd1 = document.getElementById('UserAdd1');
            const userAdd2 = document.getElementById('UserAdd2');

            userAdd1.removeAttribute('required'); // 필드에서 required 속성 제거
            userAdd2.removeAttribute('required');
            formData.delete('workYears');
            formData.delete('zipCode');
            formData.delete('location'); // 필드에서 삭제
            formData.delete('locationDetail');
            formData.delete('workPlaceName'); // workPlaceName 필드 삭제
        }


        fetch('/register/add', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    alert(data.message)
                    window.location.href = '/login/login';  // Redirect on success
                } else {
                    document.getElementById('msg').textContent = data.message; // Show error message
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function toggleTrainerFields() {
        const userType = document.getElementById('userType').value;
        const trainerFields = document.getElementById('trainerFields');

        if (userType === 'TRAINER') {
            trainerFields.style.display = 'block';
            trainerFields.querySelectorAll('input').forEach(input => {
                if (input.name === 'location' || input.name === 'locationDetail') {
                    input.setAttribute('required', 'required');
                }
            });
        } else {
            trainerFields.style.display = 'none';
            trainerFields.querySelectorAll('input').forEach(input => {
                if (input.name === 'location' || input.name === 'locationDetail') {
                    input.removeAttribute('required');
                    input.value = ''; // 값도 초기화
                }
            });
        }
    }

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = ''; // 주소_결과값이 없을 경우 공백
                var extraAddr = ''; // 참고항목

                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }

                if(data.userSelectedType === 'R'){
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                } else {
                    document.getElementById("UserAdd1").value = '';
                }

                document.getElementById('zipCode').value = data.zonecode;
                document.getElementById("UserAdd1").value = addr;
                document.getElementById("UserAdd1").value += extraAddr;
                document.getElementById("UserAdd2").focus(); // 상세주소로 포커스 이동
            }
        }).open();
    }
</script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</body>
</html>
