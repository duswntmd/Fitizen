<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>운동 검사 결과</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 700px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h1 {
            color: #333;
        }

        p {
            font-size: 18px;
            color: #555;
        }

        button {
            display: block;
            width: 100%;
            padding: 12px;
            font-size: 18px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 20px;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>운동 검사 결과</h1>
    <p id="resultText"></p>
    <button onclick="window.location.href='/findME'">다시 검사하기</button>
</div>

<script>
    // 세션 스토리지에서 결과 가져오기
    const resultText = sessionStorage.getItem('exerciseResult');
    if (resultText) {
        document.getElementById('resultText').innerHTML = resultText; // HTML로 설정하여 링크 포함
    } else {
        document.getElementById('resultText').innerText = "결과가 없습니다. 검사를 다시 시도해주세요.";
    }
</script>

</body>
</html>
