
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
    var keyword = document.getElementById('keyword').value || "";

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