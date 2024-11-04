<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="en">
<head>
    <style>
    html{
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        }
    /* 전체 body 스타일 */
    body {
    font-family: Arial, sans-serif;
    background-color: #f5f5f5;
    margin: 0;
    padding: 0;
    color: #333;
    }

    /* 헤더 */
    h1 {
    text-align: center;
    color: #444;
    margin-top: 20px;
    }

    /* 테이블 스타일 */
    table {
    width: 80%;
    margin: 20px auto;
    border-collapse: collapse;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    overflow: hidden;
    }

    /* 테이블 헤더 */
    table thead th {
    background-color: #1C1C3C;
    color: #fff;
    padding: 15px;
    text-align: left;
    font-weight: normal;
    }

    /* 테이블 바디 */
    table tbody tr {
    border-bottom: 1px solid #ddd;
    }

    table tbody tr:hover {
    background-color: #f1f1f1;
    }

    /* 테이블 데이터 셀 */
    table td {
    padding: 15px;
    text-align: center;
    }

    /* 가격 정보 */
    td:last-child {
    font-weight: bold;
    color: #007bff;
    }

    /* 수량 input 스타일 */
    input[type="number"] {
    width: 60px;
    padding: 5px;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 4px;
    }

    /* 수정 버튼 */
    input[type="submit"] {
    background-color: #283231;
    color: #fff;
    border: none;
    padding: 8px 16px;
    border-radius: 4px;
    cursor: pointer;
    }

    input[type="submit"]:hover {
    background-color: #218838;
    }

    /* 삭제 버튼 */
    form[action="cart/delete"] input[type="submit"] {
    background-color: #dc3545;
    }

    form[action="cart/delete"] input[type="submit"]:hover {
    background-color: #c82331;
    }

    /* 결제 버튼 */
    #checkoutBtn {
    display: block;
    width: 200px;
    margin: 20px auto;
    padding: 10px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    font-size: 18px;
    cursor: pointer;
    }

    #checkoutBtn:hover {
    background-color: #0056b3;
    }

    /* 반응형 처리 */
    @media (max-width: 768px) {
    table {
    width: 100%;
    }

    td, th {
    padding: 10px;
    }

    input[type="number"] {
    width: 50px;
    }

    #checkoutBtn {
    width: 100%;
    }
    }
    </style>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
</head>
<body>
<h1>${userId}님의 장바구니</h1>


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
                <input type="checkbox" name="selectedProductIds" value="${item.product.prid}" form="checkoutForm">
            </td>
            <td>
                <img src="/image/${item.product.primage}" style="width: 100px; height: 100px;">

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

<!-- 결제 폼 -->
<form id="checkoutForm" action="cart/checkout" method="post">
    <!-- 결제 버튼 -->
    <input type="submit" value="결제하기" id="checkoutBtn">
</form>


<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->

<script>
    $(document).ready(function() {
        // 결제 버튼 클릭 시 체크된 상품들만 서버로 전송
        $('#checkoutBtn').click(function(event) {
            if ($('input[name="selectedProductIds"]:checked').length === 0) {
                event.preventDefault();  // 체크된 항목이 없으면 결제 진행 안 함
                alert('결제할 상품을 선택하세요.');
            }
        });
    });
</script>

</body>

</html>
