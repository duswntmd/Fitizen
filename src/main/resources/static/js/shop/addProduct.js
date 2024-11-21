
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
