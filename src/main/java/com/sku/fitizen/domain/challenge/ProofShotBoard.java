package com.sku.fitizen.domain.challenge;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProofShotBoard {

    private int proofNum;
    private int challengeId;
    private String userId;


    private String title;
    private String photo;
    private String uPhoto;


    private List<ProofComment> commentList;


}


