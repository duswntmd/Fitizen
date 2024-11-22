package com.sku.fitizen.domain.store;

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

    @Override
    public String toString() {
        return "CartItem{" +
                "product_id=" + product_id +
                ", qty=" + qty +
                ", price=" + price +
                ", product_name='" + product_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", product_price=" + product_price +
                '}';
    }
}
