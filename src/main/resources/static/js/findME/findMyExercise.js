
    document.querySelectorAll('.exercise-img').forEach(img => {
    img.addEventListener('click', (event) => {
        const img = event.target;
        const questionGroup = img.classList[1]; // 'frequency-img'와 같은 클래스를 가져옵니다

        // 해당 질문의 이미지에서 'selected' 클래스 제거
        document.querySelectorAll(`.${questionGroup}`).forEach(image => {
            image.classList.remove('selected');
        });
        // 클릭된 이미지에 'selected' 클래스 추가
        img.classList.add('selected');

        // 해당 질문의 라디오 버튼 체크
        const associatedRadio = img.parentElement.querySelector('input[type="radio"]');
        if (associatedRadio) {
            associatedRadio.checked = true;
        }
    });
});

    function saveResultAndRedirect() {
    // 사용자가 선택한 답변을 가져오기
    const form = document.getElementById('quizForm');
    const frequency = form.elements['frequency'].value;
    const type = form.elements['type'].value;
    const goal = form.elements['goal'].value;
    const time = form.elements['time'].value;
    const environment = form.elements['environment'].value;

    const height = parseInt(form.elements['height'].value);
    const weight = parseInt(form.elements['weight'].value);

    if (!height || height <= 0 || !weight || weight <= 0) {
    alert("올바른 키와 몸무게를 입력해주세요.");
    return false; // 폼 제출 방지
}

    const scores = {
    health: 0,
    yoga: 0,
    pilates: 0,
    cardio: 0,
    swimming: 0,
    basketball: 0,
    tabletennis: 0,
    badminton: 0
};

    // 빈도에 따른 점수 계산
    switch (frequency) {
    case 'never':
    scores.yoga += 2;
    scores.pilates += 2;
    break;
    case 'sometimes':
    scores.cardio += 2;
    scores.badminton += 1;
    scores.tabletennis += 1;
    break;
    case 'regularly':
    scores.health += 2;
    scores.swimming += 2;
    scores.basketball += 1;
    break;
    case 'daily':
    scores.health += 3;
    scores.cardio += 2;
    scores.swimming += 2;
    scores.basketball += 2;
    break;
}

    // 선호 운동 유형에 따른 점수 계산
    switch (type) {
    case 'cardio':
    scores.cardio += 3;
    scores.swimming += 2;
    scores.basketball += 2;
    break;
    case 'strength':
    scores.health += 3;
    scores.badminton += 1;
    scores.tabletennis += 1;
    break;
    case 'flexibility':
    scores.yoga += 3;
    scores.pilates += 3;
    break;
    case 'balance':
    scores.pilates += 3;
    scores.yoga += 2;
    scores.badminton += 1;
    break;
}

    // 목표에 따른 점수 계산
    switch (goal) {
    case 'lose_weight':
    scores.cardio += 2;
    scores.swimming += 2;
    scores.basketball += 1;
    break;
    case 'build_muscle':
    scores.health += 3;
    if (weight > 80) {
    scores.health += 1;
} else {
    scores.badminton += 1;
}
    break;
    case 'increase_endurance':
    scores.cardio += 2;
    scores.swimming += 2;
    scores.basketball += 1;
    if (height > 175) {
    scores.swimming += 1;
} else {
    scores.cardio += 1;
}
    break;
    case 'improve_flexibility':
    scores.yoga += 3;
    scores.pilates += 3;
    if (height < 160) {
    scores.yoga += 1;
} else {
    scores.pilates += 1;
}
    break;
}

    // 운동 시간에 따른 점수 조정
    switch (time) {
    case 'short':
    scores.cardio += 1;
    scores.tabletennis += 1;
    break;
    case 'medium':
    scores.yoga += 1;
    scores.pilates += 1;
    scores.basketball += 1;
    break;
    case 'long':
    scores.health += 2;
    scores.swimming += 2;
    scores.cardio += 2;
    break;
}

    // 환경에 따른 점수 조정
    switch (environment) {
    case 'indoor':
    scores.health += 2;
    scores.yoga += 2;
    scores.pilates += 2;
    scores.tabletennis += 1;
    scores.badminton += 1;
    break;
    case 'outdoor':
    scores.cardio += 2;
    scores.swimming += 2;
    scores.basketball += 2;
    break;
    case 'both':
    scores.cardio += 1;
    scores.yoga += 1;
    scores.pilates += 1;
    scores.health += 1;
    scores.swimming += 1;
    scores.basketball += 1;
    scores.tabletennis += 1;
    scores.badminton += 1;
    break;
}

    // 추천 운동 결정
    const sortedScores = Object.entries(scores).sort(([,a], [,b]) => b - a);
    const topThree = sortedScores.slice(0, 3).map(([key]) => key);

    // 결과 텍스트 생성
    let resultText = "";
    resultText += topThree.map((sport) => {
    switch (sport) {

    case 'health': return "<a href='exerciseDetail?exercise=health'><img class='exercise-img fade-in' src='/image/health.jpg' alt='헬스'  /></a>" +
    "<div class='exercise-text'>헬스</div>";
    case 'yoga': return "<a href='exerciseDetail?exercise=yoga'><img class='exercise-img fade-in' src='/image/yoga.jpg' alt='요가' /></a>" +
    "     <div class='exercise-text'>요가</div>";
    case 'pilates': return "<a href='exerciseDetail?exercise=pilates'><img class='exercise-img fade-in' src='/image/pilates.jpg' alt='필라테스' /></a>" +
    "     <div class='exercise-text'>필라테스</div>";
    case 'cardio': return "<a href='exerciseDetail?exercise=cardio'><img class='exercise-img fade-in' src='/image/cardio.jpg' alt='유산소 운동' /></a>" +
    "     <div class='exercise-text'>유산소 운동</div>";
    case 'swimming': return "<a href='exerciseDetail?exercise=swimming'><img class='exercise-img fade-in' src='/image/swimming.jpg' alt='수영' /></a>" +
    "     <div class='exercise-text'>수영</div>";
    case 'basketball': return "<a href='exerciseDetail?exercise=basketball'><img class='exercise-img fade-in' src='/image/basketball.jpg' alt='농구' /></a>" +
    "     <div class='exercise-text'>농구</div>";
    case 'tabletennis': return "<a href='exerciseDetail?exercise=tabletennis'><img class='exercise-img fade-in' src='/image/tabletennis.jpg' alt='탁구' /></a>" +
    "     <div class='exercise-text'>탁구</div>";
    case 'badminton': return "<a href='exerciseDetail?exercise=badminton'><img class='exercise-img fade-in' src='/image/badminton.jpg' alt='배드민턴' /></a>" +
    "     <div class='exercise-text'>배드민턴</div>";
    default: return "";
}
}).join(" ");

    // 세션 스토리지에 결과 저장
    sessionStorage.setItem('exerciseResult', resultText);

    // 결과 페이지로 이동
    window.location.href = '/findResult';
    return false; // 폼 제출 방지
}


    function saveResultAndRedirectAlternate() {
    var form = document.getElementById('quizForm');
    var frequency = form.elements['frequency'].value;
    var preference = form.elements['type'].value;
    var goal = form.elements['goal'].value;
    var height = parseInt(form.elements['height'].value);
    var weight = parseInt(form.elements['weight'].value);

    if (height <= 0 || weight <= 0) {
    alert("올바른 키와 몸무게를 입력해주세요.");
    return false;
}

    var data = {
    height: height,
    weight: weight,
    frequency: frequency,
    goal: goal,
    preference: preference
};

    $.ajax({
    url: '/ai/predict_exercise',
    type: 'POST',
    contentType: 'application/json',  // JSON 형식으로 전송
    data: JSON.stringify(data),       // JavaScript 객체를 JSON 문자열로 변환하여 전송
    dataType: 'text',                 // JSON 응답을 예상
    success: function(response) {
    if (response && typeof response === 'string') {
    window.location.href ="/ai" +response; // 서버에서 반환된 URL로 이동
} else {
    alert("올바르지 않은 응답입니다. 다시 시도해주세요.");
}
},
    error: function(xhr, status, error) {
    console.error('Error status:', status);
    console.error('Error message:', error);
    console.error('Response text:', xhr.responseText);

    if (xhr.responseText.includes('<!DOCTYPE html>')) {
    alert('로그인이 필요하거나 서버에 문제가 있습니다. 다시 시도해 주세요.');
    window.location.href = '/login/login';
} else {
    alert("결과를 불러오는 데 문제가 발생했습니다. 다시 시도해주세요.");
}
}
});
    return false;
}
