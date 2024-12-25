<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="../header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MAP</title>
    <link rel="stylesheet" type="text/css" href="/css/map/map.css">
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=be5e9a482ef1bcad0ca4d349481aac7a&libraries=services"></script>
</head>

<body>
<div class="page-contents">
<form id="placeForm" action="/kakao/reviewDetail" method="post" style="display:none;">
    <input type="hidden" name="placeId" id="placeId">
    <input type="hidden" name="place_name" id="place_name">
    <input type="hidden" name="road_address_name" id="road_address_name">
    <input type="hidden" name="address_name" id="address_name">
    <input type="hidden" name="phone" id="phone">
</form>
<div class="map_wrap">
    <div id="map" style="width:100%;height:130%;position:relative;overflow:hidden;"></div>

    <div id="menu_wrap" class="bg_white">
        <div class="option">
            <div>
                <form onsubmit="searchPlaces(); return false;">
                    키워드 : <input type="text" value="${keyword}" id="keyword" size="15">
                    <button type="submit">검색하기</button>
                </form>
            </div>
        </div>
        <hr>
        <ul id="placesList"></ul>
        <div id="pagination"></div>
    </div>
</div>
</div>
<%@ include file="../footer.jsp" %> <!-- 푸터 파일 포함 -->
<script src="/js/map/map.js"></script>

</body>
</html>
