package com.sku.fitizen.domain.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rewards {

    private String userId;           // 유저 아이디
    private int challengeId;         // 챌린지 아이디
    private int rewardPoints;        // 지급 포인트
    private Date rewardDate;

    public Rewards(String userId, int challengeId, int rewardPoints) {
        this.userId = userId;
        this.challengeId = challengeId;
        this.rewardPoints = rewardPoints;
    }
}
