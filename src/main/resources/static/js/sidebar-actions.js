$(document).ready(function() {
    // 챌린지 목록을 클릭했을 때 AJAX 요청을 통해 우측에 목록을 로드
    $('#myChallengeList').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            url: '/challenge/myChall',
            method: 'GET',
            success: function(response) {
                $('.content').html(response);
            },
            error: function() {
                alert('챌린지 목록을 불러오는 데 실패했습니다.');
            }
        });
    });
});


$(document).ready(function() {
    $('#myTrainers').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            url: '/consultation/myConsultation',
            method: 'GET',
            success: function(response) {
                $('.content').html(response);
            },
            error: function() {
                alert('서버가 불안정합니다.');
            }
        });
    });

    $('#myUsers').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            url: '/consultation/myConsultation',
            method: 'GET',
            success: function(response) {
                $('.content').html(response);
            },
            error: function() {
                alert('서버가 불안정합니다.');
            }
        });
    });
});

$(document).ready(function() {
    $('#myPayments').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            url: '/getMyPayments',
            method: 'GET',
            success: function(response) {
                $('.content').html(response);
            },
            error: function() {
                alert(response);
            }
        });
    });
});

$(document).ready(function() {
    $('#myOrder').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            url: '/myOrder',
            method: 'GET',
            success: function(response) {
                $('.content').html(response);
            },
            error: function() {
                alert(response);
            }
        });
    });
});