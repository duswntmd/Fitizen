
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true"%>
<c:set var="user" value="${sessionScope.user}" />
<c:set var="loginId" value="${user == null ? '' : user.id}" />
<c:set var="loginOutLink" value="${user == null ? '/login/login' : '/login/logout'}" />

<c:choose>
    <c:when test="${user == null}">
        <c:set var="loginOut" value="Login" />
    </c:when>
    <c:otherwise>
        <c:set var="loginOut" value="${'Logout ('}${loginId}${')'}" />
    </c:otherwise>
</c:choose>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="/css/index/indexStyle.css"> <!-- CSS 파일 링크 -->
</head>

<header>
    <div class="header-container">
        <div id="logo">
            <a href="/"><img src="/image/logo.png" width="300" height="100" alt="Logo"></a>
        </div>
        <ul class="nav">
            <li class="dropdown">
                <a href="/shop">상점</a>
                <ul class="dropdown-menu">
                    <li><a href="/shop">상점</a></li>
                    <li><a href="/cart">장바구니</a></li>
                </ul>
            </li>
            <li> <a href="/findME">맞춤 운동 찾기</a></li>
            <li><a href="/trainer">트레이너</a></li>
            <li><a href="/challenge">챌린지</a></li>
            <li><a href="/ai/chatBot">챗봇</a></li>
            <li class="dropdown">
                <a href="/board/list">   게시판   </a>
                <ul class="dropdown-menu">
                    <li><a href="/board/list">자유게시판</a></li>
                    <li><a href="/qna">Q&A</a></li>


                </ul>
            </li>
            <li><a href="${loginOutLink}">${loginOut}</a></li>
            <c:if test="${user == null}">
             <li>   <a href="/register/add">회원가입</a></li>
            </c:if>
            <li class="dropdown">

                <c:if test="${user != null}">

                    <a href="/user/myPage">마이페이지</a>
                </c:if>

                <ul class="dropdown-menu">
                    <li class="flex_item"><a href="/register/updateuser">수정</a></li>
                    <li class="flex_item"><a href="/register/deleteuser">삭제</a></li>
                </ul>
            </li>



        </ul>
    </div>
</header>
<!-- 페이지 컨텐츠 -->

</html>
