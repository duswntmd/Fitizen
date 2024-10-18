<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        margin: 0;
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

    /* 버튼 스타일 */
    .page-contents a {
        display: inline-block;
        padding: 10px 20px;
        margin: 10px;
        background-color: #007BFF;
        color: white;
        text-decoration: none;
        border-radius: 5px;
        font-size: 1.1em;
        transition: background-color 0.3s ease;
    }

    .page-contents a:hover {
        background-color: #0056b3; /* 버튼에 마우스를 올렸을 때 색상 변경 */
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

<!-- 상품 설명 -->
<p>${product.prdesc}</p>

<!-- 상품 가격 -->
<p>가격: ${product.prprice}원</p>

<!-- 로그인여부 확인 후 장바구니 추가로직 -->
    <c:choose>
        <c:when test="${not empty userId}">
            <!-- 장바구니에 추가 버튼 -->
            <a href="javascript:void(0);" id="addCartBtn">장바구니에 추가</a>

        </c:when>
        <c:otherwise>
            <!-- 로그인 페이지로 이동 -->
            <a href="${pageContext.request.contextPath}/login/login">로그인 후 장바구니에 추가</a>
        </c:otherwise>
    </c:choose>
<!-- 상품 목록으로 돌아가는 버튼 -->
<br>
<a href="${pageContext.request.contextPath}/shop">상품 목록으로 돌아가기</a>
</div>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
<script>
    // 페이지 로드 시 클릭 이벤트 설정
    $(document).ready(function () {
        // id="addCartBtn" 버튼 클릭 시 addCart 함수 호출
        $('#addCartBtn').click(function () {
            addCart();  // 클릭 시 addCart 함수 호출
        });
    });

    // 장바구니 추가 함수
    function addCart() {
        $.ajax({
            url: '/shop/cart/add/${product.prid}', // 서버 측 URL (GET 요청)
            type: 'GET',
            success: function (response) {
                if (response.cartadded > 0) {
                    alert('장바구니에 성공적으로 추가되었습니다.');
                    window.location.href = '/cart';  // 장바구니 페이지로 리다이렉트
                } else {
                    alert('장바구니에 추가 실패');
                }
            },
            error: function (xhr, status, error) {
                alert('장바구니에 추가 실패: ' + error);
            }
        });
    }
</script>


</body>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</html>