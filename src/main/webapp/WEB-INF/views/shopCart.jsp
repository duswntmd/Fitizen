<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
</head>
<body>
<h1>장바구니</h1>

<p>사용자: ${userId}</p>

<table border="1" cellpadding="10" cellspacing="0">
    <thead>
    <tr>
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
            <!-- 상품명 -->
            <td>${item.product.prname}</td>
            <!-- 수량 -->
            <td>${item.qty}</td>
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



</body>
</html>
