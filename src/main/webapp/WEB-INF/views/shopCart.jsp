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
<!-- 포인트 사용 영역 -->
<div id="pointUsage" style="margin-top: 30px; text-align: center;">
    <h3>포인트 사용</h3>
    <p>보유 포인트: <span id="availablePoints">${point}</span> P</p>
    <form id="pointForm" method="post" action="cart/applyPoints">
        <label for="usePoints">사용할 포인트: </label>
        <input type="number" id="usePoints" name="usePoints" min="0" max="${point}" value="0">
        <button type="button" id="applyPoints">적용</button>
    </form>
    <p><strong>적용된 포인트:</strong> <span id="appliedPoints">0</span> P</p>
</div>

<div id="selectedItemsInfo" style="text-align: center; margin-top: 20px;">
    <h3>선택된 항목</h3>
    <ul id="selectedItemsList" style="display: none;"></ul>
    <p><strong>총 금액:</strong> <span id="totalPrice">0</span> 원</p>
</div>
<button id="checkoutBtn" onclick="requestPay()">결제하기</button>


<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
<script src="/js/shop/shopCart.js"></script>
<script>



    $(document).ready(function() {
        var IMP = window.IMP;
        IMP.init("imp38808434");// 가맹점 식별코드로 초기화
    });


    function requestPay() {


        // 적용된 포인트 값을 콘솔에 출력
        const appliedPoints = parseInt(document.getElementById("appliedPoints").textContent) || 0;

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
                price: product_price,
                product_name: product_name,
                product_price: price
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
                                orderProducts: selectedItems,
                                appliedPoints:appliedPoints
                            }),
                            dataType: 'json',
                            success: function(success) {
                                alert("결제 완료")
                                location.reload();
                                //console.log("결제 정보 저장 성공",success);
                            },
                            error: function(xhr, status, error) {
                                console.error("결제 정보 저장 실패", error);
                                location.reload();
                            }
                        });
                    } else {
                        alert("결제 실패");
                        location.reload();
                    }
                });
            });
    }

</script>
</body>

</html>

