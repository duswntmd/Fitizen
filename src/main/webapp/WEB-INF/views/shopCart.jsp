<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="/css/shop/shopCart.css">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
</head>
<body>
<h1>${user.id}님의 장바구니</h1>


<table>
    <thead>
    <tr style="text-align: center">
        <th>선택</th>
        <th></th>
        <th>상품명</th>
        <th>수량</th>
        <th>가격</th>
        <th>총 금액</th>
        <th>삭제</th>
    </tr>
    </thead>
    <tbody>
    <!-- 장바구니 항목을 반복하여 출력 -->
    <c:forEach var="item" items="${cart}">

        <tr>
            <!-- 체크박스 -->
            <td>
                <input type="checkbox" name="selectedProductIds" value="${item.product.prid}" >
            </td>
            <td>
                <img src="/ShopImage/${item.product.primage}" style="width: 100px; height: 100px;">

            </td>
            <!-- 상품명 -->
            <td>${item.product.prname}</td>
            <!-- 수량 수정 -->
            <td>
                <form action="cart/update" method="post">
                    <input type="hidden" name="productId" value="${item.product.prid}">
                    <input type="number" name="qty" value="${item.qty}" min="1">
                    <input type="submit" value="수정">
                </form>
            </td>
            <!-- 가격 -->
            <td>${item.product.prprice}</td>
            <!-- 총 금액 -->
            <td>${item.qty * item.product.prprice}</td>
            <!-- 삭제 버튼 -->
            <td>
                <form action="cart/delete" method="post">
                    <input type="hidden" name="productId" value="${item.product.prid}">
                    <input type="submit" value="삭제">
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div id="selectedItemsInfo" style="text-align: center; margin-top: 20px;">
    <h3>선택된 항목</h3>
    <ul id="selectedItemsList" style="display: none;"></ul>
    <p><strong>총 금액:</strong> <span id="totalPrice">0</span> 원</p>
</div>
    <button onclick="requestPay()">결제하기</button>



<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
<script src="/js/shop/shopCart.js"></script>
</body>

</html>
