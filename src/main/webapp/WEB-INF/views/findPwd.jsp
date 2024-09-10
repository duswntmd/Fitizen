<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function(){
            $('#findPwdForm').submit(function(event){
                event.preventDefault(); // form 기본 동작 막기

                var email = $('#email').val();
                var id = $('#id').val();

                $.ajax({
                    url: '/login/findPwd', // 컨트롤러의 매핑 URL
                    type: 'POST',
                    data: {
                        email: email,
                        id: id
                    },
                    success: function(response) {
                        if (response.pwd) {
                            $('#result').html("비밀번호는: " + response.pwd);
                        } else {
                            $('#result').html(response.message);
                        }
                    },
                    error: function() {
                        $('#result').html("오류가 발생했습니다.");
                    }
                });
            });
        });
    </script>
</head>
<body>
<h2>비밀번호 찾기</h2>
<form id="findPwdForm">
    <div>
        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" required />
    </div>
    <div>
        <label for="id">아이디:</label>
        <input type="text" id="id" name="id" required />
    </div>
    <div>
        <button type="submit">비밀번호 찾기</button>
    </div>
</form>

<div id="result"></div>
</body>
</html>
