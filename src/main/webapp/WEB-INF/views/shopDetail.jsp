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
<style>
    /* 페이지 전체 스타일 */
    html, body {
        height: 100%;
        font-family: 'Pretendard', sans-serif;
        background-color: #f4f4f9;
        color: #333;
        line-height: 1.6;
        margin: 0;
        padding: 0;
    }

    /* 메인 콘텐츠 영역 스타일 */
    .page-contents {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        flex-grow: 1;
        padding: 20px;
        margin: 20px auto; /* 가운데 정렬 */
        max-width: 800px; /* 최대 너비 설정 */
        background-color: #ffffff; /* 배경색 흰색 */
        border: 1px solid #ccc; /* 윤곽선 추가 */
        border-radius: 10px; /* 모서리 둥글게 */
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
    }

    /* 제목 스타일 */
    .page-contents h1 {
        font-size: 2.5em;
        margin-bottom: 20px;
        color: #444;
    }

    /* 상품 이름 스타일 */
    .page-contents h2 {
        font-size: 2em;
        margin-bottom: 10px;
        color: #333;
    }

    /* 상품 설명 스타일 */
    .page-contents p {
        font-size: 1.2em;
        color: #555;
        margin-bottom: 15px;
    }

    /* 가격 텍스트 스타일 */
    .page-contents p:nth-child(3) {
        font-weight: bold;
        font-size: 1.5em;
        color: #e60023; /* 강조된 가격 색상 */
        margin-bottom: 25px;
    }

    /* 버튼 스타일 통일 */
    .page-contents a {
        width:200px;
        display: inline-block;
        padding: 10px 20px;
        margin: 10px;
        background-color: #007BFF; /* 기본 버튼 색상 */
        color: white;
        text-decoration: none;
        border-radius: 5px;
        font-size: 1.1em;
        transition: background-color 0.3s ease;
        border: none; /* 버튼 테두리 제거 */
        cursor: pointer; /* 마우스 커서 포인터로 변경 */
        text-align: center; /* 버튼 텍스트 가운데 정렬 */
    }
    .page-contents button{
        width:200px;
        display: inline-block;
        padding: 10px 20px;
        margin: 10px;
        background-color:  #FF0000;; /* 기본 버튼 색상 */
        color: white;
        text-decoration: none;
        border-radius: 5px;
        font-size: 1.1em;
        transition: background-color 0.3s ease;
        border: none; /* 버튼 테두리 제거 */
        cursor: pointer; /* 마우스 커서 포인터로 변경 */
        text-align: center; /* 버튼 텍스트 가운데 정렬 */
    }
    /* 버튼 호버 스타일 */
    .page-contents a:hover{
        background-color: #0056b3; /* 버튼에 마우스를 올렸을 때 색상 변경 */
    }
    .page-contents button:hover {
        background-color: #990000; /* 버튼에 마우스를 올렸을 때 색상 변경 */
    }
    /* 상품 목록으로 돌아가기 버튼 스타일 */
    .page-contents a:nth-child(6) {
        background-color: #6c757d; /* 회색 톤 */
    }

    .page-contents a:nth-child(6):hover {
        background-color: #5a6268; /* 버튼에 마우스를 올렸을 때 색상 변경 */
    }

</style>


<body>
<div class="page-contents">
<h1>상품 상세 정보</h1>

<!-- 상품 이름 -->
<h2>${product.prname}</h2>

    <img src="/image/${product.primage}" alt="${product.prname}" style="max-width: 300px; max-height: 300px;">
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
</script>
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