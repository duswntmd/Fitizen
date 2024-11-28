package com.sku.fitizen.domain.Trainer;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Consultation {


    private  int consultId;
    private String userId;
    private int trainerNo;
    private String status; //'REQUESTED'(기본값:요청됨), 'APPROVED(승인됨)', 'REJECTED(거절됨)'

    public Consultation(String id, int trainerNo) {
        this.userId = id;
        this.trainerNo = trainerNo;
    }
}
