
    $("#deleteUserForm").submit(function(e) {
    e.preventDefault();

    $.ajax({
    url: "/register/deleteuser",
    type: "POST",
    data: {
    id: $("#id").val(),
    name: $("#name").val(),
    pwd: $("#pwd").val(),
    _csrf: $("input[name='_csrf']").val() // CSRF 토큰 포함
},
    success: function(response) {
    if (response.status === "success") {
    alert(response.message);
    window.location.href = "/";
} else {
    alert(response.message);
}
},
    error: function() {
    alert("오류가 발생했습니다. 다시 시도해주세요.");
}
});
});