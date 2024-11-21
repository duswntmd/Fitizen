
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
                    alert("임시 비밀번호가 이메일로 전송되었습니다.");
                    window.location.href="/login/login"
                } else {
                    alert("비밀번호 변경에 실패했습니다.");
                }
            },
            error: function(xhr,status,err) {
                $('#spinner').hide();
                alert("error:"+err);
            }
        });
    });
});
