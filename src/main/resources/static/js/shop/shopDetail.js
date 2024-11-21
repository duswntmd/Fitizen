
    // 페이지 로드 시 클릭 이벤트 설정
    $(document).ready(function () {
    // id="addCartBtn" 버튼 클릭 시 addCart 함수 호출
    $('#addCartBtn').click(function () {
        addCart();  // 클릭 시 addCart 함수 호출
    });
});

    function addCart() {
    var qty = $('#qty').val();  // 입력된 수량 값 가져오기
    var prid = $('#prid').val();
    var userId = $('#userId').val();
    $.ajax({
    url: '/shop/cart/add', // 서버 측 URL (POST 요청)
    type: 'POST',
    data: {
    prid: prid,  // 상품 ID
    qty: qty,  // 입력된 수량 값 전송
    userId: userId
},
    success: function (response) {

    if (response.status === 'success') {
    alert('장바구니에 성공적으로 추가되었습니다.');
    window.location.href = '/cart';  // 장바구니 페이지로 리다이렉트
} else {
    alert('장바구니에 추가 실패: ' + response.message);
}
},
    error: function (xhr, status, error) {
    alert('장바구니에 추가 실패: ' + error);
}
});
}