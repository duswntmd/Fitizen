package com.sku.fitizen.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participation {
    private String userId;
    private int ChallengeId;

}
