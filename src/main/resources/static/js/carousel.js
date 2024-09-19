const prevButton = document.querySelector('#prev'); // 이전 버튼
const nextButton = document.querySelector('#next'); // 다음 버튼
const carouselList = document.querySelector('#carousel-list'); // 캐로셀 리스트
const width = document.querySelector('.carousel-item').clientWidth; // 슬라이드 너비
const carouselItems = document.querySelectorAll('.carousel-item');
const carouselItemCount = carouselItems.length / 3; // 그룹당 슬라이드 개수 (3개 세트로 나누기)
let currentIndex = 0; // 현재 슬라이드 인덱스
let isAnimating = false; // 애니메이션 중인지 여부
let interval;

const indicators = document.querySelectorAll('.indicator'); // 인디케이터들

// 현재 슬라이드 인덱스에 맞춰 인디케이터 업데이트
const updateIndicators = (currentIndex) => {
    const normalizedIndex = currentIndex % carouselItemCount; // 슬라이드를 3개 세트로 그룹화해서 인덱스 처리
    indicators.forEach((indicator, index) => {
        if (index === normalizedIndex) {
            indicator.classList.add('active'); // 현재 인덱스에 해당하는 인디케이터 활성화
        } else {
            indicator.classList.remove('active'); // 나머지 인디케이터 비활성화
        }
    });
};

// 슬라이드를 이동하는 함수
const moveToSlide = (index) => {
    if (isAnimating) return; // 애니메이션 중에 다른 명령을 실행하지 않음
    isAnimating = true; // 애니메이션 시작

    currentIndex = index; // 현재 인덱스 업데이트
    carouselList.style.transition = 'transform 0.5s ease-out'; // 슬라이드 전환 효과 추가
    carouselList.style.transform = `translateX(${-width * (currentIndex + 1)}px)`; // 슬라이드 이동

    // 무한 스크롤 처리
    setTimeout(() => {
        if (currentIndex === -1) {
            // 첫 슬라이드에서 왼쪽으로 이동 시 마지막 슬라이드로 이동
            carouselList.style.transition = 'none';
            currentIndex = carouselItemCount * 3 - 1; // 마지막 세트로 이동
            carouselList.style.transform = `translateX(${-width * (currentIndex + 1)}px)`;
        } else if (currentIndex === carouselItemCount * 3) {
            // 마지막 슬라이드에서 오른쪽으로 이동 시 첫 번째 슬라이드로 이동
            carouselList.style.transition = 'none';
            currentIndex = 0; // 첫 번째 슬라이드로 이동
            carouselList.style.transform = `translateX(${-width}px)`;
        }
        updateIndicators(currentIndex % carouselItemCount); // 인디케이터 업데이트
        isAnimating = false; // 애니메이션 완료
    }, 500); // 애니메이션 시간 맞추기
};

// 이전 버튼 클릭 이벤트
prevButton.addEventListener('click', () => {
    moveToSlide((currentIndex - 1 + carouselItemCount * 3) % (carouselItemCount * 3)); // 슬라이드 이동
});

// 다음 버튼 클릭 이벤트
nextButton.addEventListener('click', () => {
    moveToSlide((currentIndex + 1) % (carouselItemCount * 3)); // 슬라이드 이동
});

// 인디케이터 클릭 시 슬라이드 이동
indicators.forEach((indicator, index) => {
    indicator.addEventListener('click', () => {
        moveToSlide(index); // 해당 인디케이터에 맞는 슬라이드로 이동
    });
});

// 슬라이드 아이템 복제
const cloneItems = () => {
    const firstItem = carouselItems[0].cloneNode(true); // 첫 번째 아이템 복제
    const lastItem = carouselItems[carouselItems.length - 1].cloneNode(true); // 마지막 아이템 복제
    carouselList.appendChild(firstItem); // 복제된 첫 번째 아이템을 마지막에 추가
    carouselList.insertBefore(lastItem, carouselItems[0]); // 복제된 마지막 아이템을 처음에 추가
};
cloneItems(); // 슬라이드 복제 실행

// 초기 슬라이드 위치 설정 (복제된 첫 번째 아이템을 피하기 위해 위치 설정)
carouselList.style.transform = `translateX(${-width}px)`;

// 자동 슬라이드 기능
const startAutoSlide = () => {
    interval = setInterval(() => {
        moveToSlide(currentIndex + 1); // 자동으로 다음 슬라이드로 이동
    }, 3000); // 3초 간격
};

// 자동 슬라이드 중지 기능
const stopAutoSlide = () => {
    clearInterval(interval); // 자동 슬라이드 중지
};

// 마우스 오버 시 자동 슬라이드 중지
carouselList.addEventListener('mouseover', stopAutoSlide);
carouselList.addEventListener('mouseout', startAutoSlide);

// 초기화 시 인디케이터 업데이트
updateIndicators(currentIndex);
