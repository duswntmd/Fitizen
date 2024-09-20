<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<style>
    /* 로딩 스피너 디자인 */
.spinner {
border: 4px solid rgba(0, 0, 0, 0.1);
border-left-color: #4caf50; /* 스피너 색상 */
border-radius: 50%;
width: 40px;
height: 40px;
animation: spin 1s linear infinite;
display: none; /* 기본적으로 숨김 */
margin: 0 auto;
}

@keyframes spin {
0% { transform: rotate(0deg); }
100% { transform: rotate(360deg); }
}
</style>
<style>
    /* 전체 페이지 레이아웃을 위한 설정 */
    html, body {
        height: 100%;
        margin: 0;
        display: flex;
        flex-direction: column;

    }

    /* 콘텐츠가 화면의 나머지 공간을 차지하도록 설정 */
    .page-contents {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center; /* 콘텐츠가 가운데 정렬되도록 설정 */
        align-items: center;
    }


    button {
        display: block;
        margin-top: 20px;
    }
</style>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function(){
            $('#findPwdForm').submit(function(event){
                event.preventDefault(); // form 기본 동작 막기

                var id = $('#id').val();
                var name = $('#name').val();
                $('#spinner').show();

                $.ajax({
                    url: '/mail/findPwd', // 컨트롤러의 매핑 URL
                    type: 'POST',
                    data: {

                        id: id,
                        name: name
                    },
                    dataType:'json',
                    success: function(res) {
                        $('#spinner').hide();
                        console.log(res); // 객체 전체를 확인하기 위해 콘솔에 출력

                        if (res.res) { // 응답의 "response" 필드 접근 확인
                            alert("임시 비밀번호가 발급되었습니다.");
                            window.location.href="/login/login"
                        } else {
                            alert("비밀번호 변경에 실패했습니다.");
                        }
                    },
                    error: function() {
                        $('#spinner').hide();
                        alert("error:"+error);
                    }
                });
            });
        });
    </script>
</head>
<body>

<h2>비밀번호 찾기</h2>
<div class="page-contents">
<form id="findPwdForm">

    <div>
        <label for="id">아이디:</label>
        <input type="text" id="id" name="id" required />
    </div>
    <div>
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required />
    </div>
    <div>
        <button type="submit">임시 비밀번호 발급</button>
        <div id="spinner" class="spinner"></div>
    </div>
</form>

<div id="result"></div>
</div>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>
