
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
