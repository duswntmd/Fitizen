<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>상품 추가</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column; /* 요소들을 세로로 정렬 */
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f9f9f9;
        }
        .container {
            width: 100%;
            max-width: 1000px;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);

        }
        h2 {
            text-align: center;
            color: #333;
        }
        label {
            display: block;
            margin-top: 15px;
            color: #555;
        }
        input[type="text"],
        input[type="number"],
        select,
        textarea {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="file"] {
            margin-top: 10px;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            border: none;
            color: #fff;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 20px;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>상품 추가</h2>
    <form id="productForm" enctype="multipart/form-data">
        <label for="prname">상품명:</label>
        <input type="text" id="prname" name="prname" required><br>

        <label for="prdesc">상품 설명:</label>
        <textarea id="prdesc" name="prdesc" rows="4" required></textarea><br>

        <label for="prprice">가격:</label>
        <input type="number" id="prprice" name="prprice" required><br>

        <label for="primage">상품 이미지:</label>
        <input type="file" id="primage" name="primage" accept="image/*"><br><br>

        <button type="button" onclick="submitForm()">상품 추가</button>
    </form>
    <div id="result"></div>
</div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function submitForm() {
            var formData = new FormData(document.getElementById("productForm"));

            $.ajax({
                url: '/shop/add',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    alert(response.message); // 성공 메시지를 alert로 표시
                    window.location.href = '/shop'; // shopList로 페이지 이동
                },
                error: function(xhr, status, error) {
                    $('#result').html('<p>상품 추가 중 오류가 발생했습니다. 다시 시도해주세요.</p>');
                }
            });
        }
    </script>
</body>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</html>
