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

        button {
            display: block;
            margin-top: 20px;
        }
    </style>
</head>
<body>

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
