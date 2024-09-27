package com.sku.fitizen.domain.Trainer;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    private int trainerNo;
    private String userId;
    private String workPlaceName; // 근무지
    private int zipCode; // 우편번호
    private String location; // 주소
    private String locationDetail; //상세 주소
    private int workYears; //경력사항
    private String awards;  // 수상 기록

}
