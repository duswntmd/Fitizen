package com.sku.fitizen.domain.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoVerification {

    // 사진 인증 도메인
    private int proofNum;
    private String verifierId; // 인증자 아이디
    private int challengeId; ;

}
