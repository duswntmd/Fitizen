package com.sku.fitizen.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerDTO {
    private String userId;            // 유저이름
    private int challengeId;          // 챌린지 아이디
    private int verifiedCount;        // 인증된 횟수
    private int proofCount;           // 챌린지의 필요한 인증사진 개수
    private double percentage;         // 퍼센트

    @Override
    public String toString() {
        return "SchedulerDTO{" +
                "userId='" + userId + '\'' +
                ", challengeId=" + challengeId +
                ", verifiedCount=" + verifiedCount +
                ", proofCount=" + proofCount +
                ", percentage=" + percentage +
                '}';
    }
}
