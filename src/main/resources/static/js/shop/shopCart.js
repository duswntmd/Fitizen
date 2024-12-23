
    document.addEventListener('DOMContentLoaded', function() {
    // 선택된 상품 정보와 총 금액 업데이트 함수
    function updateSelectedItems() {
        let totalPrice = 0;
        let selectedItemsList = document.getElementById('selectedItemsList');
        selectedItemsList.innerHTML = '';  // 기존 항목 초기화

        let checkboxes = document.querySelectorAll('input[name="selectedProductIds"]:checked');
        checkboxes.forEach(function(checkbox) {
            let row = checkbox.closest('tr');
            let itemName = row.querySelector('td:nth-child(3)').innerText;  // 상품명
            let itemTotalPrice = parseFloat(row.querySelector('td:nth-child(6)').innerText.replace(/,/g, ''));  // 총 금액 (쉼표 제거)

            totalPrice += itemTotalPrice;

            // 선택된 상품 목록에 추가
            let listItem = document.createElement('li');
            listItem.textContent = itemName + ' - ' + itemTotalPrice.toLocaleString() + '원';
            selectedItemsList.appendChild(listItem);
        });

        // 총 금액 업데이트
        document.getElementById('totalPrice').textContent = totalPrice.toLocaleString();
    }

    // 체크박스 상태 변경 시 이벤트 (즉각적인 업데이트)
    document.querySelectorAll('input[name="selectedProductIds"]').forEach(function(checkbox) {
    checkbox.addEventListener('change', updateSelectedItems);
});
});

    document.getElementById("applyPoints").addEventListener("click", function () {
        // 현재 사용 가능한 포인트와 입력된 포인트 값 가져오기
        const availablePoints = parseInt(document.getElementById("availablePoints").textContent) || 0;
        const usePoints = parseInt(document.getElementById("usePoints").value) || 0;

        // 총 금액 가져오기
        const totalPriceElement = document.getElementById("totalPrice");
        const totalPrice = parseInt(totalPriceElement.textContent.replace(/[^0-9]/g, "")) || 0;

        // 이미 적용된 포인트 가져오기
        const appliedPoints = parseInt(document.getElementById("appliedPoints").textContent) || 0;


        // 최대 5000원까지만 사용 가능
        if (usePoints > 5000) {
            alert("포인트는 최대 5000원까지 사용 가능합니다.");
            return;
        }
        // 입력된 포인트가 사용 가능한 포인트를 초과하는 경우 처리
        if (usePoints > availablePoints) {
            alert("사용 가능한 포인트를 초과할 수 없습니다.");
            return;
        }

        // 입력된 포인트가 총 금액보다 큰 경우 처리
        if (usePoints > totalPrice) {
            alert("사용 포인트가 총 금액을 초과할 수 없습니다.");
            return;
        }

        // 기존에 적용된 포인트를 초기화
        const restoredTotalPrice = totalPrice + appliedPoints; // 이전에 뺀 포인트를 더함
        document.getElementById("totalPrice").textContent = restoredTotalPrice.toLocaleString();

        // 새로 입력된 포인트로 적용
        document.getElementById("appliedPoints").textContent = usePoints;
        const updatedTotalPrice = restoredTotalPrice - usePoints; // 새로운 포인트를 적용
        totalPriceElement.textContent = updatedTotalPrice.toLocaleString();

        alert("포인트가 적용되었습니다.");
    });
