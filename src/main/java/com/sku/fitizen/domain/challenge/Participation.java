package com.sku.fitizen.domain.challenge;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participation {
    private String userId;
    private int challengeId;

    private int spentPoint;

    public Participation(String userId, int spentPoint) {
        this.userId = userId;
        this.spentPoint = spentPoint;
    }

    public Participation(int challengeId, String userId) {
        this.challengeId = challengeId;
        this.userId = userId;
    }
}
