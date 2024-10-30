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
    <style>
        /* 콘텐츠가 화면의 나머지 공간을 차지하도록 설정 */
        html, body {
            height: 100%;
            margin: 0;
            font-family: 'Pretendard', sans-serif; /* 사용자가 설정한 Pretendard 폰트 사용 */
            background-color: #f4f4f9; /* 전체 배경을 은은한 회색으로 */
            color: #333; /* 텍스트 색상을 다크 그레이로 */
            line-height: 1.6; /* 가독성을 위한 라인 높이 설정 */
            overflow-y: auto; /* 페이지 자체에서 스크롤 */
        }

        .page-contents {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh; /* 페이지의 전체 높이 */
        }

        .form-container {
            width: 100%;
            max-width: 900px;
            background-color: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        input[type="text"], input[type="password"], input[type="email"], input[type="date"], input[type="number"], select {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        button {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }

        button:hover {
            background-color: #0056b3;
        }

        .label {
            font-weight: bold; /* 라벨을 볼드체로 */
            margin-bottom: 4px; /* 라벨 아래 약간의 여백 */
        }

        #trainerFields {
            background-color: #ffffff; /* 배경을 흰색으로 */
            border: 1px solid #e0e0e0; /* 약간의 회색 테두리 */
            padding: 15px; /* 여백 추가 */
            margin-top: 20px; /* 상단 여백 */
            border-radius: 8px; /* 부드러운 모서리 */
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 약간의 그림자 추가 */
        }

        input::placeholder {
            color: #aaa; /* 플레이스홀더 색상을 회색으로 변경 */
        }

        select {
            appearance: none; /* 기본 드롭다운 화살표 제거 */
            background-image: url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2210%22%20height%3D%2210%22%3E%3Cpath%20d%3D%22M0%200%20L5%205%20L10%200%22%20fill%3D%22%23000000%22%3E%3C/path%3E%3C/svg%3E');
            background-repeat: no-repeat;
            background-position: right 10px center;
            padding-right: 30px; /* 화살표가 들어갈 공간 추가 */
        }

        input[type="button"] {
            background-color: #f0f0f0; /* 밝은 회색 배경 */
            border: 1px solid #ccc; /* 회색 테두리 */
            padding: 8px 12px; /* 패딩 추가 */
            cursor: pointer; /* 커서를 포인터로 변경 */
            font-size: 14px; /* 글자 크기 설정 */
            border-radius: 4px; /* 모서리 둥글게 */
            transition: background-color 0.3s ease; /* 배경색 변환 효과 */
        }

        input[type="button"]:hover {
            background-color: #e0e0e0; /* 마우스를 올렸을 때 약간 진한 회색 */
        }

        .msg {
            color: red; /* 오류 메시지를 빨간색으로 */
            margin-bottom: 10px; /* 메시지 아래 여백 추가 */
        }
    </style>
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
