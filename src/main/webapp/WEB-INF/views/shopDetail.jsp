<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="ko">
<html>
<head>
    <title>상품 상세 정보</title>
</head>
<link rel="stylesheet" type="text/css" href="/css/shop/shopDetail.css">



<body>
<div class="page-contents">
<h1>상품 상세 정보</h1>

<!-- 상품 이름 -->
<h2>${product.prname}</h2>

    <img src="/ShopImage/${product.primage}" alt="${product.prname}" style="max-width: 300px; max-height: 300px;">
<!-- 상품 설명 -->
    <p>${product.prdesc}</p>

<!-- 상품 가격 -->
<p>가격: ${product.prprice}원</p>
    <!-- 수량 입력 필드 -->
    <label for="qty">수량:</label>
    <input type="number" id="qty" name="qty" min="1" value="1"> <!-- 기본 수량을 1로 설정 -->
    <input type="hidden" id="prid" value="${product.prid}">
    <input type="hidden" id="userId" value="${user.id}">
<!-- 로그인여부 확인 후 장바구니 추가로직 -->

            <!-- 장바구니에 추가 버튼 -->

    <a href="javascript:void(0);" id="addCartBtn" class="button">장바구니에 추가</a>
    <br>
    <a href="${pageContext.request.contextPath}/shop" class="button">상품 목록으로 돌아가기</a>
    <button id="deleteButton" class="button">삭제</button>
</div>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
<script>
    $(document).ready(function () {
        // id="addCartBtn" 버튼 클릭 시 addCart 함수 호출
        $('#addCartBtn').click(function () {
            var qty = $('#qty').val();  // 입력된 수량 값 가져오기
            var prid = $('#prid').val(); // 상품 ID 가져오기
            var userId = $('#userId').val(); // 사용자 ID 가져오기

            console.log("수량:", qty, "상품 ID:", prid, "사용자 ID:", userId);

            // addCart 함수 호출 시 값 전달
            addCart(prid, qty, userId);
        });
    });

    function addCart(prid, qty, userId) {
        $.ajax({
            url: '/shop/cart/add', // 서버 측 URL (POST 요청)
            type: 'POST',
            data: {
                prid: prid,  // 상품 ID
                qty: qty,    // 입력된 수량 값 전송
                userId: userId // 사용자 ID
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
</script>

<script src="/js/shop/shopDetail_del.js"></script>

</body>

</html>