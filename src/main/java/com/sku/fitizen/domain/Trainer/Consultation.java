package com.sku.fitizen.domain.Trainer;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Consultation {


    private  int consultId;
    private String userId;
    private int trainerNo;
    private String status; //'REQUESTED'(기본값), 'APPROVED', 'REJECTED', 'EXPIRED'
    private String isPaid; // Y,N(기본값)
    public Consultation(String id, int trainerNo) {
        this.userId = id;
        this.trainerNo = trainerNo;
    }
}
