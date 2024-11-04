<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png"/>
    <title>사용자 삭제</title>
    <style>
        /* 전체 페이지 레이아웃을 위한 설정 */
        html, body {
            height: 100%;
            margin: 0;
            font-family: 'Pretendard', sans-serif;
            background-color: #f4f4f9; /* 은은한 배경 색상 */
            color: #333; /* 다크 그레이 텍스트 색상 */
        }

        /* body에 헤더 높이만큼 패딩을 추가 */
        #bodyContainer {
            max-width: 900px;
            margin: 50px auto;
            padding-top: 100px; /* 헤더 높이만큼 패딩 추가 */
            padding-bottom: 100px ;
        }

        /* 페이지 콘텐츠를 가운데로 배치 */
        .page-contents {
            background-color: #fff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 약간의 그림자 효과 */
        }

        /* 폼 필드 설정 */
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px;
            margin: 35px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 16px;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus, input[type="password"]:focus {
            border-color: #007bff;
            outline: none;
        }

        /* 버튼 스타일 */
        button {
            width: 100%;
            background-color: #007bff;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #0056b3;
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }

    </style>
</head>
<body>

<div id="bodyContainer">
<div class="page-contents">
    <h2>사용자 삭제</h2>
    <form id="deleteUserForm">
        <label for="id">아이디:</label>
        <input type="text" id="id" name="id" required>
        <br>
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required>
        <br>
        <label for="pwd">비밀번호:</label>
        <input type="password" id="pwd" name="pwd" required>
        <br>
        <button type="submit">사용자 삭제</button>
    </form>
</div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $("#deleteUserForm").submit(function(e) {
        e.preventDefault();

        $.ajax({
            url: "${pageContext.request.contextPath}/register/deleteuser",
            type: "POST",
            data: {
                id: $("#id").val(),
                name: $("#name").val(),
                pwd: $("#pwd").val()
            },
            success: function(response) {
                if (response.status === "success") {
                    alert(response.message);
                    window.location.href = "/";
                } else {
                    alert(response.message);
                }
            },
            error: function() {
                alert("오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    });
</script>

<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>
