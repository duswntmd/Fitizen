
    console.log('${exercise1}', '${confidence_score1}', '${exercise2}', '${confidence_score2}', '${exercise3}', '${confidence_score3}');

    // Chart.js를 이용하여 원형 차트 생성 및 클릭 이벤트 추가
    const ctx = document.getElementById('exerciseChart').getContext('2d');
    const exerciseChart = new Chart(ctx, {
    type: 'pie',
    data: {
    labels: exercises,
    datasets: [{
    data: confidenceScores, // 각 운동의 정확도
    backgroundColor: ['#4CAF50', '#FF7043', '#29B6F6'], // 각 운동에 대한 색상
    borderWidth: 1
}]
},
    options: {
    responsive: false, // 반응형을 비활성화하여 고정된 크기 유지
    maintainAspectRatio: false, // 차트 비율을 고정하지 않음
    onClick: (event, elements) => { // 최상위에 위치
    if (elements.length > 0) {
    const index = elements[0].index;
    const label = exerciseChart.data.labels[index];
    window.location.href = exerciseLinks[label];
}
},
    plugins: {
    legend: {
    display: true,
    position: 'bottom'
},
    datalabels: {
    color: '#ffffff', // 텍스트 색상
    font: {
    weight: 'bold',
    size: 14
},
    formatter: (value, context) => {
    const exercise = context.chart?.data?.labels?.[context.dataIndex] || "Unknown Exercise";
    return exercise+":"+ value+"%"; // 운동 이름과 % 값 표시
},
    // 각 데이터 포인트별 높이 조정
    offset: (context) => {
    if (context.dataIndex === 1) { // 운동2
    return 10; // 기본 위치에서 약간 위로 이동
} else if (context.dataIndex === 2) { // 운동3
    return 40; // 기본 위치에서 약간 아래로 이동
}
    return 0; // 기본 위치
},
    anchor: 'end', // 데이터 레이블의 위치 설정
    align: 'start' // 데이터 레이블 정렬
}
}
},
    plugins: [ChartDataLabels] // datalabels 플러그인 활성화
});
