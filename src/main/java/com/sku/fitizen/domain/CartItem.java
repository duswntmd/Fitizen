package com.sku.fitizen.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CartItem {
    private int product_id;
    private int qty;
    private int price;
    private String product_name;
    private String user_id;
    private Product product;
    private int product_price;


}
