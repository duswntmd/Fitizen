<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상점</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 18px;
            text-align: left;
        }
        th, td {
            padding: 12px;
            border: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }

        .page-contents {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: center; /* 콘텐츠가 가운데 정렬되도록 설정 */
            align-items: center;
            padding-bottom: 0;
            margin-bottom: 0;
        }
        html, body {
            font-family: 'Pretendard', sans-serif; /* 사용자가 설정한 Pretendard 폰트 사용 */
            background-color: #f4f4f9; /* 전체 배경을 은은한 회색으로 */
            color: #333; /* 텍스트 색상을 다크 그레이로 */
            line-height: 1.6; /* 가독성을 위한 라인 높이 설정 */
            margin:0;

        }
    </style>
    <script>

    </script>
</head>
<body>
<div class="page-contents">
    <div>
        <h1> 상점 페이지</h1>
    </div>

    <div id="main">

        <table>
            <tr>
                <th>이미지</th>
                <th>상품 번호</th>  <!-- tr의 첫번째 자식 -->
                <th>상품명</th>
                <th>가격</th>

            </tr>

            <c:forEach var="pr" items="${list}" varStatus="status">

                <tr>
                    <td><img src="../image/${pr.primage}" style="max-width: 100px; max-height: 100px;"></td>
                    <td> ${pr.prid}</td>
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