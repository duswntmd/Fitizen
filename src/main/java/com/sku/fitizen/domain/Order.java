package com.sku.fitizen.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Order {
    private String user_id;
    private int product_id;
    private int qty;
    private int price;
    private Date orderDate;
}
