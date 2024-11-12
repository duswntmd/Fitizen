<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %> <!-- 헤더 파일 포함 -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>아이디 찾기</title>
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
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function(){
            $('#findIdForm').submit(function(event){
                event.preventDefault(); // form의 기본 제출 동작을 막음

                var email = $('#email').val();
                var name = $('#name').val();

                $.ajax({
                    url: '/mail/findId', // 컨트롤러의 매핑 URL
                    type: 'POST',
                    data: {
                        email: email,
                        name: name
                    },
                    success: function(response) {
                        if (response.id) {
                            $('#result').html("아이디는 " + response.id+"입니다");
                        } else {
                            $('#result').html(response.message);
                        }
                    },
                    error: function(xhr,status,err) {
                        $('#result').html("오류가 발생했습니다."+err);
                    }
                });
            });
        });
    </script>
</head>
<body>
<div class="page-contents">
<h2>아이디 찾기</h2>
<form id="findIdForm">
    <div>
        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" required />
    </div>
    <div>
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required />
    </div>
    <div>
        <button type="submit">아이디 찾기</button>
    </div>
</form>

<div id="result"></div>
<div>
    <a href="/login/login"><button>로그인 화면으로 돌아가기</button></a>
</div>
</div>
<%@ include file="footer.jsp" %> <!-- 푸터 파일 포함 -->
</body>
</html>
