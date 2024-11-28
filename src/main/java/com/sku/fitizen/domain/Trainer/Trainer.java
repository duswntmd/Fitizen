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
    private String userName;
    private String workPlaceName; // 근무지
    private int zipCode; // 우편번호
    private String location; // 주소
    private String locationDetail; //상세 주소
    private int workYears; //경력사항
    private char approved; //승인 상태
    private String introduction;  // 소개글
    private String introductionDetail;
    private String profileImage ;
    private String backGroundImage ;

    @Override
    public String toString() {
        return "Trainer{" +
                "trainerNo=" + trainerNo +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", workPlaceName='" + workPlaceName + '\'' +
                ", zipCode=" + zipCode +
                ", location='" + location + '\'' +
                ", locationDetail='" + locationDetail + '\'' +
                ", workYears=" + workYears +
                ", approved=" + approved +
                ", introduction='" + introduction + '\'' +
                ", introductionDetail='" + introductionDetail + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", backGroundImage='" + backGroundImage + '\'' +
                '}';
    }
}
