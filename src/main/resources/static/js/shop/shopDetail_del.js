

document.getElementById("deleteButton").addEventListener("click", function() {


    if (confirm("정말로 삭제하시겠습니까?")) {
        $.ajax({
            url: '/shop/delete/' + productId, // 상품 ID에 따라 URL 변경
            type: 'DELETE',
            success: function(response) {
                alert(response); // 성공 메시지 표시
                window.location.href = '/shop'; // 삭제 후 목록으로 이동
            },
            error: function(xhr, status, error) {
                alert("상품 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    }
});
