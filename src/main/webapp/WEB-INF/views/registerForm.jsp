<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false"%>
<c:set var="loginId" value="${pageContext.request.getSession(false)==null ? '' : pageContext.request.session.getAttribute('id')}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? '로그인' : 'ID='+=loginId}"/>
<%@ page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>sungkyul</title>

    <title>Register</title>
</head>
<body>


<!-- form action="<c:url value="/register/save"/>" method="POST" onsubmit="return formCheck(this)"-->

<form:form modelAttribute="user">
    <div class="title">Register</div>
    <div id="msg" class="msg"><form:errors path="id"/></div>
    <div pwd="msg" class="msg"><form:errors path="pwd"/></div>
    <span style="color:red;">${message}</span>
    <label for="">아이디</label>
    <input class="input-field" type="text" name="id" placeholder="4~10자리의 영대소문자와 숫자 조합">
    <label for="">비밀번호</label>
    <input class="input-field" type="text" name="pwd" placeholder="4~10자리의 영대소문자와 숫자 조합">
    <label for="">이름</label>
    <input class="input-field" type="text" name="name" placeholder="홍길동">
    <label for="">이메일</label>
    <input class="input-field" type="text" name="email" placeholder="example@gmail.co.kr">
    <label for="">생일</label> <!-- text -->
    <input class="input-field" type="date" name="birth" placeholder="2020-12-31">

    <button>회원 가입</button>
</form:form>
<%--</form>--%>
<script>
    let msg = "${msg}";
    // 만약 msg 값이 "WRT_OK"라면 성공적으로 회원가입되었다는 알림창을 띄움
    if(msg === "WRT_OK") {
        alert("성공적으로 회원가입 되었습니다.");
    }

    function formCheck(frm) {
        var msg ='';
        if(frm.id.value.length<3) {
            setMessage('id의 길이는 3이상이어야 합니다.', frm.id);
            return false;
        }
        return true;
    }
    function setMessage(msg, element){
        document.getElementById("msg").innerHTML = `<i class="fa fa-exclamation-circle"> ${'${msg}'}</i>`;
        if(element) {
            element.select();
        }
    }



</script>
</body>
</html>