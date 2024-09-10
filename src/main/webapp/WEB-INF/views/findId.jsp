<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>아이디 찾기</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function(){
            $('#findIdForm').submit(function(event){
                event.preventDefault(); // form의 기본 제출 동작을 막음

                var email = $('#email').val();
                var name = $('#name').val();

                $.ajax({
                    url: '/login/findId', // 컨트롤러의 매핑 URL
                    type: 'POST',
                    data: {
                        email: email,
                        name: name
                    },
                    success: function(response) {
                        if (response.id) {
                            $('#result').html("아이디는: " + response.id);
                        } else {
                            $('#result').html(response.message);
                        }
                    },
                    error: function() {
                        $('#result').html("오류가 발생했습니다."+err);
                    }
                });
            });
        });
    </script>
</head>
<body>
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
</body>
</html>
