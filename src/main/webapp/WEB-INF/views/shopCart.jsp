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
<div id="selectedItemsInfo" style="text-align: center; margin-top: 20px;">
    <h3>선택된 항목</h3>
    <ul id="selectedItemsList" style="display: none;"></ul>
    <p><strong>총 금액:</strong> <span id="totalPrice">0</span> 원</p>
</div>
    <button onclick="requestPay()">결제하기</button>



<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
<script>


    $(document).ready(function() {
        var IMP = window.IMP;
        IMP.init("imp38808434");// 가맹점 식별코드로 초기화
    });


    function requestPay() {

            if ($('input[name="selectedProductIds"]:checked').length === 0) {
                alert('결제할 상품을 선택하세요.');
                return;
            }

        const userEmail = '${user.email}';
        const userId = '${user.id}';
        let totalPrice = parseFloat(document.getElementById('totalPrice').textContent.replace(/,/g, '')) || 0;

        const selectedItems = [];
        $('input[name="selectedProductIds"]:checked').each(function () {
            const row = $(this).closest('tr');
            const qty = parseInt(row.find('input[name="qty"]').val(), 10); // 수량
            const price = parseFloat(row.find('td:nth-child(5)').text().replace(/,/g, '')); // 개별 가격
            const product_name = row.find('td:nth-child(3)').text();
            const product_price =parseFloat(row.find('td:nth-child(6)').text().replace(/,/g, '')); // 계산된 총 가격
            //console.log(row+"/"+qty+"/"+price+"/"+product_name+"/"+product_price)
            selectedItems.push({
                product_id: $(this).val(),
                qty: qty,
                price: price,
                product_name: product_name,
                product_price: product_price
            });
            //console.log(selectedItems)
        });

        IMP.init("imp38808434");
        IMP.request_pay(
            {
                pg: 'html5_inicis',
                name: "FITIZEN STORE",
                amount: totalPrice,
                buyer_email:userEmail,
                buyer_name:userId

            },
            function (rsp) {
                console.log(rsp);
                $.ajax({
                    type: 'POST',
                    url: '/verify/' + rsp.imp_uid
                }).done(function(data) {
                    console.log(data);
                    if (rsp.paid_amount === data.response.amount) {
                        //alert("imp_uid: " + data.impUid + " merchant_uid(orderKey) :" + data.merchantUid);
                        //console.log("결제 및 결제검증완료")

                        // 결제 검증 성공 후, 추가 데이터 전송
                        $.ajax({
                            type: 'POST',
                            url: '/orderPayment',
                            contentType: 'application/json',
                            data: JSON.stringify({
                                userId: userId,
                                totalPrice:totalPrice,
                                impUid: rsp.imp_uid,
                                merchantUid: rsp.merchant_uid,
                                paidAt: data.paidAt,
                                orderProducts: selectedItems
                            }),
                            dataType: 'json',
                            success: function(success) {
                                alert("결제 완료")
                                //console.log("결제 정보 저장 성공",success);
                            },
                            error: function(xhr, status, error) {
                                console.error("결제 정보 저장 실패", error);
                            }
                        });
                    } else {
                        alert("결제 실패");
                    }
                });
            });
    }




    document.addEventListener('DOMContentLoaded', function() {
        // 선택된 상품 정보와 총 금액 업데이트 함수
        function updateSelectedItems() {
            let totalPrice = 0;
            let selectedItemsList = document.getElementById('selectedItemsList');
            selectedItemsList.innerHTML = '';  // 기존 항목 초기화

            let checkboxes = document.querySelectorAll('input[name="selectedProductIds"]:checked');
            checkboxes.forEach(function(checkbox) {
                let row = checkbox.closest('tr');
                let itemName = row.querySelector('td:nth-child(3)').innerText;  // 상품명
                let itemTotalPrice = parseFloat(row.querySelector('td:nth-child(6)').innerText.replace(/,/g, ''));  // 총 금액 (쉼표 제거)

                totalPrice += itemTotalPrice;

                // 선택된 상품 목록에 추가
                let listItem = document.createElement('li');
                listItem.textContent = itemName + ' - ' + itemTotalPrice.toLocaleString() + '원';
                selectedItemsList.appendChild(listItem);
            });

            // 총 금액 업데이트
            document.getElementById('totalPrice').textContent = totalPrice.toLocaleString();
        }

        // 체크박스 상태 변경 시 이벤트 (즉각적인 업데이트)
        document.querySelectorAll('input[name="selectedProductIds"]').forEach(function(checkbox) {
            checkbox.addEventListener('change', updateSelectedItems);
        });
    });
</script>

</body>

</html>
