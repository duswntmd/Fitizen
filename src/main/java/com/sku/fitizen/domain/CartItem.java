package com.sku.fitizen.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class CartItem {
    private int product_id;
    private int qty;
    private int price;
    private String product_name;
    private String user_id;
    private Product product;


}
