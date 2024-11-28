
// 뒤로가기 또는 앞으로가기로 탭 상태 복원
window.onpopstate = function (event) {
        if (event.state && event.state.tab) {
            const tabId = event.state.tab;
            $('#' + tabId).trigger('click');
        } else {
            console.log('No state in popstate event');
        }
    };

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
$(document).ready(function() {
    $('#getTrainers').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            url: '/admin/getTrainers',
            method: 'GET',
            success: function(response) {
                $('.content').html(response);
            },
            error: function() {
                alert(response);
            }
        });
    });
})

$(document).ready(function() {
    $('#updateInfo').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            url: '/trainer/InfoPage',
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
$(document).ready(function () {
    /*
        // URL에서 activeTab 파라미터 가져오기
        const urlParams = new URLSearchParams(window.location.search);
        const urlActiveTab = urlParams.get('activeTab');
        // URL에서 activeTab 값이 있으면 세션 스토리지에 저장하고 해당 탭 로드
        if (urlActiveTab) {
            console.log('Active Tab from URL:', urlActiveTab);
            sessionStorage.setItem('activeTab', urlActiveTab); // 세션 스토리지 덮어쓰기
            loadTabContent(urlActiveTab);
        }
    */

    // 사이드바에서 탭 클릭 시 상태 저장 및 AJAX 요청 처리
    $(document).on('click', '#myTrainers, #myUsers, #myChallengeList, #myPayments,#getTrainers, #myOrder,#updateInfo', function () {
        const tabId = this.id;

        // 세션 스토리지에 탭 ID 저장
        sessionStorage.setItem('activeTab', tabId);

        // 히스토리 상태 업데이트
        history.pushState({ tab: tabId }, '', window.location.href);

        // AJAX 요청 처리
        loadTabContent(tabId);
    });

    // 초기 로딩 시 저장된 탭 복원
    const activeTab = sessionStorage.getItem('activeTab');
    if (activeTab) {
        loadTabContent(activeTab);
    }

    // AJAX 요청 처리 함수
    function loadTabContent(tabId)
    {
        switch (tabId) {
            case 'myTrainers':
                loadMyTrainers();
                break;
            case 'myUsers':
                loadMyUsers();
                break;
            case 'myChallengeList':
                loadMyChallenges();
                break;
            case 'myPayments':
                loadMyPayments();
                break;
            case 'myOrder':
                loadMyOrders();
                break;
            case 'getTrainers' :
                loadMyGetTrainers();
                break;
            case 'updateInfo' :
                loadUpdateInfo();
                break;
            default:
                console.log('Unknown Tab ID:', tabId);
        }
    }

    // AJAX 요청 함수들
    function loadMyTrainers() {
        $.ajax({
            url: '/consultation/myConsultation',
            method: 'GET',
            success: function (response) {
                $('.content').html(response);
            },
            error: function () {
                alert('서버가 불안정합니다.');
            }
        });
    }

    function loadMyUsers() {
        $.ajax({
            url: '/consultation/myConsultation',
            method: 'GET',
            success: function (response) {
                $('.content').html(response);
            },
            error: function () {
                alert('서버가 불안정합니다.');
            }
        });
    }

    function loadMyChallenges() {
        $.ajax({
            url: '/challenge/myChall',
            method: 'GET',
            success: function (response) {
                $('.content').html(response);
            },
            error: function () {
                alert('챌린지 목록을 불러오는 데 실패했습니다.');
            }
        });
    }

    function loadMyPayments() {
        $.ajax({
            url: '/getMyPayments',
            method: 'GET',
            success: function (response) {
                $('.content').html(response);
            },
            error: function () {
                alert('결제 내역을 불러오는 데 실패했습니다.');
            }
        });
    }

    function loadMyOrders() {
        $.ajax({
            url: '/myOrder',
            method: 'GET',
            success: function (response) {
                $('.content').html(response);
            },
            error: function () {
                alert('주문 내역을 불러오는 데 실패했습니다.');
            }
        });
    }

    function loadMyGetTrainers() {
        $.ajax({
            url: '/admin/getTrainers',
            method: 'GET',
            success: function (response) {
                $('.content').html(response);
            },
            error: function () {
                alert('트레이너 목록 불러오기 실패.');
            }
        });
    }

    function loadUpdateInfo() {
        $.ajax({
            url: '/trainer/InfoPage',
            method: 'GET',
            success: function (response) {
                $('.content').html(response);
            },
            error: function () {
                alert('트레이너 목록 불러오기 실패.');
            }
        });
    }


});



