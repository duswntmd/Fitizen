# FITIZEN  AI 프로젝트 

<details>
<summary>목차</summary>

- [프로젝트 소개](#1-프로젝트-소개)
- [팀원 소개](#2-팀원-소개)
- [개발 기간](#3-개발-기간)
- [개발 환경 및 기술 스택](#4-개발-환경-및-기술-스택)
- [역할 분담](#5-역할-분담)
- [서비스 정보 구조도](#6-서비스-정보-구조도)
- [서비스 기능 소개](#7-서비스-기능-소개)
- [느낀점](#8-느낀점)

</details>

---

## 1. 프로젝트 소개

<img width="1705" alt="스크린샷 2024-12-03 오후 7 02 23" src="https://github.com/user-attachments/assets/4bcf76c8-cc43-4977-abb2-ddcf9f907d70">

  <p>초보자를 위한 운동 웹사이트</p>
  <p>
    운동 루틴부터 식단 계획, <br>
    그리고 운동 방법까지, <br>
    초보자도 쉽게 따라할 수 있는 맞춤 정보를 제공합니다.임시 문구임
  </p>

> ### [🚀 배포 URL](https://fitizen.store )

| **테스트 계정**   | **아이디**    | **비밀번호** |
|------------|---------------|--------------|
| 트레이너   | trainer1      | trainer1     |
| 유저       | user1         | user1        |

<sub>2024.12.18 기준으로 서버 종료됨</sub>

## 2. 팀원 소개

|연주승|정재호|박성재|
|:---:|:---:|:---:|
|[<img alt="연주승 프로필" src="https://github.com/user-attachments/assets/dd9639d0-70a4-424b-8060-2ffe21b0725b" width="200" height="200"> <br/> @duswntmd](https://github.com/duswntmd)|[<img alt="정재호 프로필" src="https://github.com/user-attachments/assets/063b5649-ea79-466b-9976-5c677e91d599" width="200" height="200"> <br/> @JaehoHoya](https://github.com/JaehoHoya)|[<img alt="박성재 프로필" src="https://github.com/user-attachments/assets/645d27c7-617c-4397-b527-f10bf110fb24" img width="200" height="200"> <br/> @psjae98](https://github.com/psjae98)|
| 팀장 |팀원|팀원|

### 😁 팀 목표
 - 개발을 할때 항상 사용자관점을 생각하며 개발하기!
 - 오류 발생시 같이 논의하기 
 - 초보자들이 운동에 대해 흥미와 정보를 얻을 수 있게 사이트 구축하기 

<br>

## 3.📆 개발 기간

### 2024년 09월 09일 ~ 2024 12월 18일 
<img width="913" alt="스크린샷 2024-12-05 오전 11 36 21" src="https://github.com/user-attachments/assets/fd5641cc-7d0b-4b16-87b3-b152f4b2dc84">

## 4. 개발 환경 및 기술 스택

<img width="970" alt="스크린샷 2024-12-18 오전 11 11 23" src="https://github.com/user-attachments/assets/a54208b3-1d75-4557-8b62-89c18b88e6a7" />


### 기술 스택

<table>
<tr>
 <td align="center">Front-End</td>
 <td>
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">
  <img src="https://img.shields.io/badge/HTML-E34F26?style=for-the-badge&logo=HTML5&logoColor=white"/>&nbsp
  <img src="https://img.shields.io/badge/CSS-1572B6?style=for-the-badge&logo=CSS3&logoColor=white"/>&nbsp
  <img src="https://img.shields.io/badge/Axios-white?style=for-the-badge&logo=Axios&logoColor=black"/>&nbsp 
   <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white">
 </td>
</tr>
<tr>
 <td align="center">협업</td>
 <td>
    <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/>&nbsp
    <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"/>
 </td>
</tr>
<tr>
 <td align="center">IDE</td>
 <td>
   인텔리제이
</tr>
 <td align="center">배포</td>
 <td>
     <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/>
 </td>
</tr>
</table>


<br>


## 5. 역할 분담
---
```sql
연주승
      - 도메인 fitizen.store 
      - 프로젝트 총괄 및 점검 ,병합 ,깃헙 관리   
      - 로그인 및 회원가입(일반유저)
      - 운동시설리뷰 
      - 게시판 ( 게시판 하위에 댓글이 있는겁니다)
      - 시큐리티
      - logBack
      - AI : 운동 자세분석 
      - 메인페이지(최종 점검 및 수정)
      - FastApi(파이썬 서버 구축) ,AWS

정재호
      - 로그인 (트레이너)
      - 마이페이지
      - 깃헙 관리 (리드미 , 위키 작성)
      - 챌린지
      - 인증 게시판
      - 트레이너 
      - 채팅
      - AI: 챗봇 
      - 결제 ,결제 기록 
      - Nginx ,AWS

박성재
      - 회원 가입, 로그인, 아이디/비밀번호 찾기 css
      - 비밀번호 변경 메일전송
      - 운동 추천(일반,AI)
      - Q&A 게시판
      - 상점, 장바구니

```
# AI 상세설명 
https://github.com/duswntmd/Fitizen/wiki/AI

<br>



## 6. 서비스 정보 구조도
---

<br>



## 7.🔧 서비스 기능 소개

- 연주승 
- 박성재
- 정재호

## 8. 느낀점

#### 연주승
- FITIZEN 프로젝트에서 팀장으로서 전체 진행 상황을 관리하고 방향을 제시하는 역할을 맡으며 책임감의 중요성을 깊이 느꼈습니다. 프로젝트가 원활히 진행되기 위해서는 팀원들이 각자의 역할을 명확히 이해하고 서로 협력하며 문제를 해결할 수 있어야 한다고 생각했습니다. 이를 위해 전반적인 상황을 꾸준히 체크하고 팀원들과의 토의를 통해 각자가 맡은 역할과 목표를 명확히 전달하는 데 주력했습니다. 특히 팀원 간의 관점을 조율하고 서로 다른 생각들을 조화롭게 엮어내는 과정이 중요했습니다. 문제 상황에서는 다양한 가설을 함께 고민하며 팀원들의 문제 해결 능력을 끌어내기 위해 자율성을 최대한 보장했습니다. 필요할 때 도움을 주되 팀원들이 스스로 해결책을 찾을 수 있도록 자신감을 심어주는 것이 더 큰 성장을 이끌어낸다는 점을 깨달았습니다. 또한 프로젝트의 일정이 타이트했던 만큼 우선순위를 명확히 설정하고 작업을 효율적으로 분배하는 것이 중요했습니다. 한 사람에게 일이 몰리지 않도록 균형을 맞추고 기능의 중요한 작업에 집중시켜야 프로젝트를 효과적으로 관리할 수 있음을 배웠습니다. 이러한 과정은 팀원들 간의 협력과 신뢰를 강화하고 프로젝트를 성공적으로 완수할 수 있는 원동력이 되었습니다. FITIZEN 프로젝트를 통해 단순히 결과물을 만들어내는 것 이상의 중요한 가치를 배울 수 있었습니다. 팀워크와 리더십의 중요성을 다시 한번 깨닫고 이번 경험이 앞으로의 목표를 이루는 데 중요한 밑거름이 되어 더욱 발전할 수 있었던 계기가 되었다고 생각합니다.
#### 정재호 
 -  리드미 다꾸미고 넣을거임 
#### 박성재
 - 프로젝트 협업은 처음이라서 처음에는 긴장도 되고, 협업 과정에서 부담을 느끼기도 했습니다. 하지만 다행히 팀원들의 적극적인 도움과 협조 덕분에 점차 부담을 덜 수 있었고, 프로젝트를 성공적으로 마칠 수 있었습니다. 서로 도움을 주고받으면서 각자의 강점을 살리고 부족한 부분을 채워나가는 과정을 통해, 단순히 결과물을 완성하는 것을 넘어 개인적으로도 많은 성장을 이룰 수 있었습니다. 특히 개발 도중 예상치 못한 이슈가 생겼을 때, 팀원들과 함께 문제를 논의하고 해결 방안을 찾아가는 과정이 인상적이었습니다. 이러한 과정에서 협업의 중요성과 혼자가 아닌 팀으로 일하는 프로젝트의 장점을 확실히 느낄 수 있었습니다. 각자의 관점에서 제안하는 다양한 아이디어를 통해 문제를 더 효과적으로 해결할 수 있었고, 이 경험이 협업 능력을 한 단계 끌어올리는 계기가 되었습니다.
 물론 아쉬운 점도 있었습니다. 초기 단계에서 요구사항이 명확히 정리되지 않아 작업 우선순위가 혼란스러웠던 적이 있었습니다. 이로 인해 중간에 설계를 수정하거나 작업 방향을 변경해야 했던 경우도 있었지만, 이러한 경험 덕분에 요구사항 정의와 초기 설계의 중요성을 깊이 깨달을 수 있었습니다. 앞으로는 이러한 시행착오를 줄이기 위해 프로젝트 시작 단계에서 더 철저히 준비하고자 합니다.
 이 프로젝트를 통해 Spring Boot 웹 개발 활용법을 실질적으로 익힐 수 있었을 뿐만 아니라, 협업 과정에서 발생할 수 있는 다양한 문제를 해결하며 더욱 성장할 수 있었습니다. 특히 팀워크의 중요성과 함께 일하는 즐거움을 느꼈기에, 앞으로 더 큰 규모의 프로젝트에서도 이러한 경험을 바탕으로 팀원들과 효과적으로 협력하며, 더 발전된 결과를 만들어낼 수 있을 것이라는 자신감을 얻었습니다.
 이번 협업은 단순한 프로젝트 완성을 넘어 개인과 팀 모두에게 의미 있는 성장을 가져다 준 값진 경험이었으며, 앞으로의 개발 여정에서도 큰 자산이 될 것입니다.
