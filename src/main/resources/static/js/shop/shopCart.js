

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
