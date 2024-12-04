<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상점</title>
    <link rel="stylesheet" type="text/css" href="/css/shop/shopList.css">
    <script>

    </script>
</head>
<body>
<div class="page-contents">
    <div>
        <h1> 상점 페이지</h1>
    </div>
    <c:if test="${userRole == 'ROLE_ADMIN'}">
            <a href="shop/addProduct">상품 추가하기</a>
    </c:if>
    <div id="main">

        <table>
            <tr>
                <th>이미지</th>

                <th>상품명</th>
                <th>가격</th>

            </tr>

            <c:forEach var="pr" items="${list}" varStatus="status">

                <tr>
                    <td><img src="../ShopImage/${pr.primage}" style="max-width: 200px; max-height: 200px;"></td>
                    <td> <a href= "shop/shopDetail/${pr.prid}">${pr.prname}</a></td>
                    <td> ${pr.prprice}</td>

                </tr>


            </c:forEach>
        </table>

    </div>

</div>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->

</body>
</html>