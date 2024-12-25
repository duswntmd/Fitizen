package com.sku.fitizen.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String pwd;
    private String name;
    private String email;
    private Date birth;
    private Date reg_date;
    private Date up_date;
    private String is_trainer; // Y,N(기본값)
    private String authority;
    private int enabled;

    private String workPlaceName; // 근무지
    private int zipCode; // 우편번호
    private String location; // 주소
    private String locationDetail; //상세 주소
    private int workYears; //경력사항
    private String awards;  // 수상 기록


}