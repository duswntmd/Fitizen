const carouselList = document.querySelector('#carousel-list');
const width = document.querySelector('.carousel-item').clientWidth;
const carouselItemCount = document.querySelectorAll('.carousel-item').length / 3;

let moveTranslateX = 0;
let currentTranslateX = 0;
let nextTranslateX = 0;
let isMove = false;
let moveStartX = 0;
const moveGap = 22;
let dragEndTime = new Date().getTime();
let interval;
let timeout;

// drag 시작 이벤트
const dragStart = (clientX) => {
    isMove = true;
    moveStartX = clientX;
    clearInterval(interval);
    clearTimeout(timeout);

    carouselList.classList.remove('carousel-list-transition');

    const dragEndStartGapTime = new Date().getTime() - dragEndTime;
    let dragEndStartGapTranslateX = 0;
    if (dragEndStartGapTime <= 600) {
        dragEndStartGapTranslateX = (nextTranslateX - currentTranslateX) * ((600 - dragEndStartGapTime) / 600 / 1.5);
    }

    // 드래그 종료 시점에서 카루젤 위치 조정
    currentTranslateX = -(((-currentTranslateX / width) % carouselItemCount) + carouselItemCount) * width + dragEndStartGapTranslateX;
    nextTranslateX = currentTranslateX;
    carouselList.style.transform = `translateX(${currentTranslateX}px)`;
}

// drag 중 이벤트
const dragging = (clientX) => {
    if (isMove) {
        moveTranslateX = clientX - moveStartX;
        nextTranslateX = currentTranslateX + moveTranslateX;

        // 오른쪽으로 최대 이동한 경우
        if (nextTranslateX < -width * (carouselItemCount * 3 - 1)) {
            nextTranslateX = -width * (carouselItemCount * 3 - 1);
        }
        // 왼쪽으로 최대 이동한 경우
        else if (nextTranslateX > 0) {
            nextTranslateX = 0;
        }

        carouselList.style.transform = `translateX(${nextTranslateX}px)`;
    }
}

// drag 종료 이벤트
const dragEnd = () => {
    if (isMove) {
        isMove = false;
        moveStartX = 0;
        carouselList.classList.add('carousel-list-transition');
        dragEndTime = new Date().getTime();
        timerConfig();

        // 드래그 후 카루젤 위치 조정
        if (currentTranslateX > nextTranslateX) {
            if ((currentTranslateX - nextTranslateX) % width > moveGap) {
                currentTranslateX = -(Math.floor(-nextTranslateX / width) + 1) * width;
            } else {
                currentTranslateX = -(Math.floor(-nextTranslateX / width)) * width;
            }
        }
        else if (currentTranslateX < nextTranslateX) {
            if ((nextTranslateX - currentTranslateX) % width > moveGap) {
                currentTranslateX = -(Math.floor(-nextTranslateX / width)) * width;
            } else {
                currentTranslateX = -(Math.floor(-nextTranslateX / width) + 1) * width;
            }
        }
        else {
            if (Math.abs(currentTranslateX) % width >= width / 2) {
                currentTranslateX = -(Math.floor(-currentTranslateX / width) + 1) * width;
            } else {
                currentTranslateX = -(Math.floor(-currentTranslateX / width)) * width;
            }
        }

        carouselList.style.transform = `translateX(${currentTranslateX}px)`;
    }
}

// carousel 자동 이동 설정
const timer = () => {
    timeout = setTimeout(() => {
        carouselList.classList.remove('carousel-list-transition');
        currentTranslateX = -(((-currentTranslateX / width) % carouselItemCount) + carouselItemCount) * width;
        carouselList.style.transform = `translateX(${currentTranslateX}px)`;
    }, 1500);

    carouselList.classList.add('carousel-list-transition');
    currentTranslateX -= width;
    carouselList.style.transform = `translateX(${currentTranslateX}px)`;

    dragEndTime = new Date().getTime();
}

// 자동 이동 timer 설정
const timerConfig = () => {
    interval = setInterval(() => {
        timer();
    }, 3000);
}

timerConfig();

// 이벤트 리스너 설정 (PC)
carouselList.addEventListener('mousedown', (e) => dragStart(e.clientX));
window.addEventListener('mousemove', (e) => dragging(e.clientX));
window.addEventListener('mouseup', dragEnd);

// 이벤트 리스너 설정 (Mobile)
carouselList.addEventListener('touchstart', (e) => dragStart(e.targetTouches[0].clientX));
window.addEventListener('touchmove', (e) => dragging(e.targetTouches[0].clientX));
window.addEventListener('touchend', dragEnd);
