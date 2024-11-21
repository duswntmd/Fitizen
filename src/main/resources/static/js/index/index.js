
    let currentSection = 0;
    const sections = document.querySelectorAll('section');
    const navDots = document.querySelectorAll('.nav-dot');

    // 섹션 위치에 따라 헤더 표시 여부 업데이트
    function checkHeaderVisibility() {
    const section1Top = section1.getBoundingClientRect().top;
    const section1Bottom = section1.getBoundingClientRect().bottom;

    if (section1Top <= 0 && section1Bottom > 0) {
    header.classList.remove('hidden'); // 섹션 1에 있을 때 헤더 표시
} else {
    header.classList.add('hidden'); // 다른 섹션에서는 헤더 숨김
}
}

    function updateNavDots() {
    navDots.forEach(dot => dot.classList.remove('active'));
    navDots[currentSection].classList.add('active');
}

    // 스크롤 이벤트 처리 함수
    window.addEventListener('wheel', function(event) {
    if (event.deltaY > 0) { // 아래로 스크롤
    currentSection = Math.min(currentSection + 1, sections.length - 1);
} else { // 위로 스크롤
    currentSection = Math.max(currentSection - 1, 0);
}
    sections[currentSection].scrollIntoView({ behavior: 'smooth' });
    updateNavDots(); // 네비게이션 점 업데이트
});

    // 점 네비게이션 클릭 이벤트
    navDots.forEach(dot => {
    dot.addEventListener('click', function() {
        currentSection = parseInt(dot.getAttribute('data-section'));
        sections[currentSection].scrollIntoView({ behavior: 'smooth' });
        updateNavDots();
    });
});

    updateNavDots(); // 초기 점 업데이트
