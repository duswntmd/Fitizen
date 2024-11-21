
    function submitForm(event) {
    event.preventDefault(); // Prevent the default form submission

    const formData = new FormData(document.getElementById('registerForm'));
    const userType = document.getElementById('userType').value;

    formData.append('is_trainer', userType === 'TRAINER' ? 'Y' : 'N');

    if (userType === 'USER') {
    // 일반 유저일 경우 location과 locationDetail 필드를 제거
    const userAdd1 = document.getElementById('UserAdd1');
    const userAdd2 = document.getElementById('UserAdd2');

    userAdd1.removeAttribute('required'); // 필드에서 required 속성 제거
    userAdd2.removeAttribute('required');
    formData.delete('workYears');
    formData.delete('zipCode');
    formData.delete('location'); // 필드에서 삭제
    formData.delete('locationDetail');
    formData.delete('workPlaceName'); // workPlaceName 필드 삭제
}


    fetch('/register/add', {
    method: 'POST',
    body: formData
})
    .then(response => response.json())
    .then(data => {
    if (data.status === 'success') {
    alert(data.message)
    window.location.href = '/login/login';  // Redirect on success
} else {
    document.getElementById('msg').textContent = data.message; // Show error message
}
})
    .catch(error => console.error('Error:', error));
}

    function toggleTrainerFields() {
    const userType = document.getElementById('userType').value;
    const trainerFields = document.getElementById('trainerFields');

    if (userType === 'TRAINER') {
    trainerFields.style.display = 'block';
    trainerFields.querySelectorAll('input').forEach(input => {
    if (input.name === 'location' || input.name === 'locationDetail') {
    input.setAttribute('required', 'required');
}
});
} else {
    trainerFields.style.display = 'none';
    trainerFields.querySelectorAll('input').forEach(input => {
    if (input.name === 'location' || input.name === 'locationDetail') {
    input.removeAttribute('required');
    input.value = ''; // 값도 초기화
}
});
}
}

    function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소_결과값이 없을 경우 공백
            var extraAddr = ''; // 참고항목

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if(data.userSelectedType === 'R'){
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
            } else {
                document.getElementById("UserAdd1").value = '';
            }

            document.getElementById('zipCode').value = data.zonecode;
            document.getElementById("UserAdd1").value = addr;
            document.getElementById("UserAdd1").value += extraAddr;
            document.getElementById("UserAdd2").focus(); // 상세주소로 포커스 이동
        }
    }).open();
}