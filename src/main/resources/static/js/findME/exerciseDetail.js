
    // URL에서 운동 종목을 가져옴
    const urlParams = new URLSearchParams(window.location.search);
    const exercise = urlParams.get('exercise');
    console.log('콘솔테스트');
    console.log('저장된운동:'+exercise);
    // 운동 설명과 영상 URL 매핑
    const exerciseDetails = {
    health: {
    title: "헬스",
    description: "근육을 강화하고 몸을 탄탄하게 만들어주는 운동으로, 자신감 넘치는 몸매를 얻는 데 도움이 됩니다.",
    videoUrl: "https://www.youtube.com/embed/xzeE7pdI3bg?si=5yiVcIDHp47jirsA"
},
    yoga: {
    title: "요가",
    description: "유연성과 마음의 평화를 동시에 얻을 수 있는 운동으로, 몸과 마음의 힐링을 경험해보세요.",
    videoUrl: "https://www.youtube.com/embed/OBTl49bVk94?si=DkMVaSGw1GugrNmb"
},
    pilates: {
    title: "필라테스",
    description: "코어 근육을 단단하게 하고 자세를 교정해, 강하고 균형 잡힌 몸을 만들어줍니다.",
    videoUrl: "https://www.youtube.com/embed/sb51gF18cYo?si=h84tk_-iZ2p15Dmr"
},
    cardio: {
    title: "유산소 운동",
    description: "심장이 튼튼해지고 에너지가 넘치는 기분을 느끼며 체중도 관리할 수 있는 최고의 선택입니다.",
    videoUrl: "https://www.youtube.com/embed/OoytN1a8Klc?si=pJzJgXH5FzIP9rgZ"
},
    swimming: {
    title: "수영",
    description: "전신을 사용해 효과적으로 칼로리를 소모하고 근육을 강화하는 운동으로, 물속에서의 자유를 만끽하세요.",
    videoUrl: "https://www.youtube.com/embed/DC8MYG6uzSs?si=29JYRUjD-iU_fs78"
},
    basketball: {
    title: "농구",
    description: "친구들과 함께 활기차게 뛰고 공을 던지며 재미와 운동을 동시에 즐길 수 있습니다.",
    videoUrl: "https://www.youtube.com/embed/uqtHD41RldE?si=ZIaC-5zwh4QYrBEi"
},
    tabletennis: {
    title: "탁구",
    description: "빠른 반사 신경과 집중력을 키우며, 즐겁게 운동할 수 있는 재미있는 스포츠입니다.",
    videoUrl: "https://www.youtube.com/embed/bvhH1NIZGA4?si=I8aqJL_0PAmyqZ2T"
},
    badminton: {
    title: "배드민턴",
    description: "빠른 속도와 기술을 요구하는 운동으로, 신나는 게임을 통해 체력과 민첩성을 기를 수 있습니다.",
    videoUrl: "https://www.youtube.com/embed/JZOeAeIkrgY?si=gBmTj_yT6NVEhqdG"
}
};

    if (exercise && exerciseDetails[exercise]) {
    const details = exerciseDetails[exercise];
    document.getElementById('exerciseTitle').innerText = details.title;
    document.getElementById('exerciseDescription').innerText = details.description;
    document.getElementById('exerciseVideo').src = details.videoUrl;
} else {
    document.getElementById('exerciseTitle').innerText = "운동 정보를 찾을 수 없습니다.";
    document.getElementById('exerciseDescription').innerText = "잘못된 운동 종목이 선택되었습니다.";
}

    function findExerciseFacility() {
    // 선택된 운동 종목을 URL에 추가하여 전송
    if (exercise) {
    window.location.href = `/kakao/map?exercise=`+exercise;
}
}
