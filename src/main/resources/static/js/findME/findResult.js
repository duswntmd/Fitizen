
    // 세션 스토리지에서 결과 가져오기
    const resultText = sessionStorage.getItem('exerciseResult');
    if (resultText) {
    document.getElementById('resultText').innerHTML = resultText; // HTML로 설정하여 링크 포함

    window.addEventListener('load', () => {
    document.querySelectorAll('.fade-in').forEach((img) => {
    img.classList.add('show');
});
});
} else {
    document.getElementById('resultText').innerText = "결과가 없습니다. 검사를 다시 시도해주세요.";
}
