<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Footer</title>
    <link rel="stylesheet" type="text/css" href="/css/index/indexstyle.css"> <!-- CSS 파일 링크 -->
    <style>
        .footer-container {
            width: 80%;
            margin: 0 auto;
            text-align: center;
        }
        .footer-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .footer-list li {
            display: flex;
            justify-content: space-between;
            padding: 5px 0;

        }
        .footer-list li:last-child {
            border-bottom: none;
        }
        .footer-list li a {

            color: inherit;
        }
        .footer-list li span {
            flex: 1;
            text-align: center;
        }
        footer p {
            margin-top: 20px;
            font-size: 0.9em;
            color: #fff;
        }
        .footer-link{

        }
    </style>
</head>
<body>

<footer>
    <div class="footer-container">
        <ul class="footer-list">
            <li>
                <span>박성재</span>
                <span>pjkart5@naver.com</span>
                <span><a class="footer-link" href="/findME">맞춤운동찾기</a></span>
                <span><a class="footer-link" href="/shop">상점</a></span>
            </li>
            <li>
                <span>연주승</span>
                <span>wnend1010@naver.com</span>
                <span><a class="footer-link" href="/board/list">게시판</a></span>
                <span><a class="footer-link" href="/ai/uploadVideo">자세분석</a></span>
            </li>
            <li>
                <span>정재호</span>
                <span>1010wogh@naver.com</span>
                <span><a class="footer-link" href="/ai/chatBot">챗봇</a></span>
                <span><a class="footer-link" href="/trainer">트레이너</a></span>
            </li>
        </ul>
        <p>&copy; 2024 FITIZEN. All rights reserved.</p>
    </div>
</footer>

</body>
</html>
