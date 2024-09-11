<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" sizes="96x96" href="/image/favicon-96x96.png"/>
    <title>MAP</title>
    <style>
        @font-face {
            font-family: 'Pretendard-Bold';
            src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Bold.woff') format('woff');
            font-weight: 700;
            font-style: normal;
        }
        body, script {
            font-family: 'Pretendard-Bold';
        }
        html, body {
            margin:0; height:100%; overflow:hidden;
        }

        .flex_container {
            display: flex;
            flex-direction: row;     /* 메인축(main axis) */
            flex-wrap: wrap;         /* 디폴트인 nowrap 대신 wrap 설정 */
            justify-content: space-around;
            align-content:space-around ;
            border:1px solid black;
            background-color: black;
            padding: 0.1em;
        }

        .nav {
            list-style: none;
            font-weight: bold;
            margin-bottom: 10px;
            float: left; /* Clear floats */
            width: 100%;
            /* Bring the nav above everything else--uncomment if needed.
            position: relative;
            z-index: 5;
            */
        }
        .nav li {
            float: left;
            margin-right: 10px;
            position: relative;
        }
        .nav a {
            display: block;
            padding: 5px;
            color: #fff;
            background-color: #000;
            text-decoration: none;
        }
        .nav a:hover {
            color: #fff;
            background-color: #6b0c36;
            text-decoration: underline;
        }

        /*--- DROPDOWN ---*/
        .nav ul {
            background-color: #fff; /* Adding a background makes the dropdown work properly in IE7+. Make this as close to your page's background as possible (i.e. white page == white background). */
            background: rgba(255,255,255,0); /* But! Let's make the background fully transparent where we can, we don't actually want to see it if we can help it... */
            list-style: none;
            position: absolute;
            left: -9999px; /* Hide off-screen when not needed (this is more accessible than display: none;) */
        }
        .nav ul li {
            padding-top: 1px; /* Introducing a padding between the li and the a give the illusion spaced items */
            float: none;
        }
        .nav ul a {
            white-space: nowrap; /* Stop text wrapping and creating multi-line dropdown items */
        }
        .nav li:hover ul { /* Display the dropdown on hover */
            left: -40px; /* 리스트 안에 있는 하위 리스트는 40px 들여쓰기 되어 나타나므로 부모와 왼쪽을 정렬하기 위함 */
        }
        .nav li:hover a { /* These create persistent hover states, meaning the top-most link stays 'hovered' even when your cursor has moved down the list. */
            background-color: #000;
            text-decoration: underline;
        }
        .nav li:hover ul a { /* The persistent hover state does however create a global style for links even before they're hovered. Here we undo these effects. */
            text-decoration: none;
        }
        .nav li:hover ul li a:hover { /* Here we define the most explicit hover states--what happens when you hover each individual link. */
            background-color: #333;
        }
        @media screen and (min-width: 300px) {
            div > nav {
                padding: 24px 30px;
            }
        }
        .map_wrap, .map_wrap * {margin:0;padding:0;font-family:'Malgun Gothic',dotum,'돋움',sans-serif;font-size:12px;}
        .map_wrap a, .map_wrap a:hover, .map_wrap a:active{color:#000;text-decoration: none;}
        .map_wrap {position:relative;width:100%;height:600px;}
        #menu_wrap {position:absolute;top:0;left:0;bottom:0;width:250px;margin:10px 0 30px 10px;padding:5px;overflow-y:auto;background:rgba(255, 255, 255, 0.7);z-index: 1;font-size:12px;border-radius: 10px;}
        .bg_white {background:#fff;}
        #menu_wrap hr {display: block; height: 1px;border: 0; border-top: 2px solid #5F5F5F;margin:3px 0;}
        #menu_wrap .option{text-align: center;}
        #menu_wrap .option p {margin:10px 0;}
        #menu_wrap .option button {margin-left:5px;}
        #placesList li {list-style: none;}
        #placesList .item {position:relative;border-bottom:1px solid #888;overflow: hidden;cursor: pointer;min-height: 65px;}
        #placesList .item span {display: block;margin-top:4px;}
        #placesList .item h5, #placesList .item .info {text-overflow: ellipsis;overflow: hidden;white-space: nowrap;}
        #placesList .item .info{padding:10px 0 10px 55px;}
        #placesList .info .gray {color:#8a8a8a;}
        #placesList .info .jibun {padding-left:26px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png) no-repeat;}
        #placesList .info .tel {color:#009900;}
        #placesList .item .markerbg {float:left;position:absolute;width:36px; height:37px;margin:10px 0 0 10px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png) no-repeat;}
        #pagination {margin:10px auto;text-align: center;}
        #pagination a {display:inline-block;margin-right:10px;}
        #pagination .on {font-weight: bold; cursor: default;color:#777;}
    </style>
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=be5e9a482ef1bcad0ca4d349481aac7a&libraries=services"></script>
</head>
<body>
<div class="flex_container">
    <li id="logo"><a href="/"><img src="/image/logo.png" width="300" height="100"></a></li>
    <nav id="menu"><a href="/"><img src="/image/MenuBar.png" width="38" height="38"></a></nav>
</div>
<h1>MAP</h1>
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
                    키워드 : <input type="text" value="" id="keyword" size="15">
                    <button type="submit">검색하기</button>
                </form>
            </div>
        </div>
        <hr>
        <ul id="placesList"></ul>
        <div id="pagination"></div>
    </div>
</div>

<script type="text/javascript">
    var map;
    var markers = [];
    var infowindow = new kakao.maps.InfoWindow({zIndex:1});

    // 사용자의 현재 위치를 받아옵니다.
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            var lat = position.coords.latitude;  // 위도
            var lng = position.coords.longitude; // 경도

            var mapContainer = document.getElementById('map'), // 지도를 표시할 div
                mapOption = {
                    center: new kakao.maps.LatLng(lat, lng), // 지도의 중심좌표를 사용자의 현재 위치로 설정합니다.
                    level: 3 // 지도의 확대 레벨
                };

            // 지도를 생성합니다
            map = new kakao.maps.Map(mapContainer, mapOption);

            // 사용자의 현재 위치에 마커를 표시합니다.
            var marker = new kakao.maps.Marker({
                position: new kakao.maps.LatLng(lat, lng),
                map: map
            });

            infowindow.setContent('<div style="padding:5px;">현재 위치</div>');
            infowindow.open(map, marker);

            searchPlaces();

        }, function (error) {
            console.error("Error Code: " + error.code + ", Message: " + error.message);
            alert("위치 정보를 가져올 수 없습니다.");
        }, { enableHighAccuracy: true, timeout: 5000, maximumAge: 0 });
    } else {
        alert('GPS를 지원하지 않습니다.');
    }

    // 장소 검색 객체를 생성합니다
    var ps = new kakao.maps.services.Places();

    // 키워드로 장소를 검색합니다
    function searchPlaces() {
        var keyword = document.getElementById('keyword').value || "헬스장";

        if (!keyword.replace(/^\s+|\s+$/g, '')) {
            alert('키워드를 입력해주세요!');
            return false;
        }

        // 키워드로 장소를 검색합니다
        ps.keywordSearch(keyword, placesSearchCB, {location: map.getCenter()});
    }

    // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
    function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            displayPlaces(data);
            displayPagination(pagination);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            alert('검색 결과가 존재하지 않습니다.');
            return;
        } else if (status === kakao.maps.services.Status.ERROR) {
            alert('검색 결과 중 오류가 발생했습니다.');
            return;
        }
    }

    // 검색 결과 목록과 마커를 표출하는 함수입니다
    function displayPlaces(places) {
        var listEl = document.getElementById('placesList'),
            menuEl = document.getElementById('menu_wrap'),
            fragment = document.createDocumentFragment(),
            bounds = new kakao.maps.LatLngBounds();

        // 검색 결과 목록에 추가된 항목들을 제거합니다
        removeAllChildNods(listEl);

        // 지도에 표시되고 있는 마커를 제거합니다
        removeMarker();

        for (var i = 0; i < places.length; i++) {
            var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                marker = addMarker(placePosition, i),
                itemEl = getListItem(i, places[i]);

            bounds.extend(placePosition);

            (function(marker, title, place) {
                kakao.maps.event.addListener(marker, 'mouseover', function() {
                    displayInfowindow(marker, title);
                });

                kakao.maps.event.addListener(marker, 'mouseout', function() {
                    infowindow.close();
                });

                // 마커 클릭 시 폼 제출
                kakao.maps.event.addListener(marker, 'click', function() {
                    submitPlaceForm(place);
                });

                itemEl.onmouseover =  function () {
                    displayInfowindow(marker, title);
                };

                itemEl.onmouseout =  function () {
                    infowindow.close();
                };

                // 리스트 아이템 클릭 시 폼 제출
                itemEl.onclick = function () {
                    submitPlaceForm(place);
                };
            })(marker, places[i].place_name, places[i]);

            fragment.appendChild(itemEl);
        }

        listEl.appendChild(fragment);
        menuEl.scrollTop = 0;

        map.setBounds(bounds);
    }

    // 검색결과 항목을 Element로 반환하는 함수입니다
    function getListItem(index, place) {
        var el = document.createElement('li'),
            itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
                '<div class="info">' +
                '   <h5>' + place.place_name + '</h5>';

        if (place.road_address_name) {
            itemStr += '    <span>' + place.road_address_name + '</span>' +
                '   <span class="jibun gray">' +  place.address_name  + '</span>';
        } else {
            itemStr += '    <span>' +  place.address_name  + '</span>';
        }

        itemStr += '  <span class="tel">' + place.phone  + '</span>' +
            '</div>';

        el.innerHTML = itemStr;
        el.className = 'item';

        return el;
    }

    function submitPlaceForm(place) {
        document.getElementById('placeId').value = place.id;
        document.getElementById('place_name').value = place.place_name;
        document.getElementById('road_address_name').value = place.road_address_name || '';
        document.getElementById('address_name').value = place.address_name;
        document.getElementById('phone').value = place.phone || '';
        document.getElementById('placeForm').submit();
    }

    // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
    function addMarker(position, idx) {
        var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png',
            imageSize = new kakao.maps.Size(36, 37),
            imgOptions =  {
                spriteSize : new kakao.maps.Size(36, 691),
                spriteOrigin : new kakao.maps.Point(0, (idx*46)+10),
                offset: new kakao.maps.Point(13, 37)
            },
            markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
                position: position,
                image: markerImage
            });

        marker.setMap(map);
        markers.push(marker);

        return marker;
    }

    // 지도 위에 표시되고 있는 마커를 모두 제거합니다
    function removeMarker() {
        for ( var i = 0; i < markers.length; i++ ) {
            markers[i].setMap(null);
        }
        markers = [];
    }

    // 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
    function displayPagination(pagination) {
        var paginationEl = document.getElementById('pagination'),
            fragment = document.createDocumentFragment(),
            i;

        while (paginationEl.hasChildNodes()) {
            paginationEl.removeChild (paginationEl.lastChild);
        }

        for (i=1; i<=pagination.last; i++) {
            var el = document.createElement('a');
            el.href = "#";
            el.innerHTML = i;

            if (i===pagination.current) {
                el.className = 'on';
            } else {
                el.onclick = (function(i) {
                    return function() {
                        pagination.gotoPage(i);
                    }
                })(i);
            }

            fragment.appendChild(el);
        }
        paginationEl.appendChild(fragment);
    }

    // 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
    function displayInfowindow(marker, title) {
        var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

        infowindow.setContent(content);
        infowindow.open(map, marker);
    }

    // 검색결과 목록의 자식 Element를 제거하는 함수입니다
    function removeAllChildNods(el) {
        while (el.hasChildNodes()) {
            el.removeChild (el.lastChild);
        }
    }
</script>
</body>
</html>
