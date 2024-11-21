
    document.getElementById("updateUserForm").onsubmit = function(event) {
    event.preventDefault();
    let formData = new FormData(this);
    fetch("/register/updateuser", {
    method: "POST",
    body: formData
})
    .then(response => response.json())
    .then(data => {
    if (data.status === "success") {
    alert(data.message);
    window.location.href = "/";
} else {
    alert("오류: " + data.message);
}
})
    .catch(error => {
    console.error("Error:", error);
    alert("사용자 정보 수정 중 오류가 발생했습니다.");
});
};