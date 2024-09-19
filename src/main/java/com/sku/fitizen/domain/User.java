package com.sku.fitizen.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@NoArgsConstructor
public class User {
    private String id;
    private String pwd;
    private String name;
    private String email;
    private Date birth;
    private Date reg_date;
    private Date up_date;

    private int webMoney;
}