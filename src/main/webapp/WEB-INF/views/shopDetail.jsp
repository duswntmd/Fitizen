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
    document.getElementById("deleteButton").addEventListener("click", function() {
        const productId = ${prid};

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
</script>

</body>

</html>