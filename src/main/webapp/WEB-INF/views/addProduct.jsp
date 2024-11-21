<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>상품 추가</title>
    <link rel="stylesheet" type="text/css" href="/css/product/addProduct.css">
    <script src="/js/shop/addProduct.js"></script>
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

</body>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</html>
