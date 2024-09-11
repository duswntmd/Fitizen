package com.sku.fitizen.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {

    // 챌린지 고유 번호 PK
    private int challengeId;
    // 챌린지 제목
    private String title;
    // 표지 이미지
    private String coverImg;
    private String uCoverImg;
    // 챌린지 기간: 시작일과 종료일
    private LocalDate startDate;
    private LocalDate endDate;
    // 인원수 제한
    private int limitUser;
    // 작성자 아이디   User FK
    private String creatorId;
    // 챌린지 내용
    private String content;

    private int members;
}
