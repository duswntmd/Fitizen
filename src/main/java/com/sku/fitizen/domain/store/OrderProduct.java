package com.sku.fitizen.domain.store;

import lombok.Getter;

@Getter
public class OrderProduct {

    private int productId;
    private int qty;
    private String productName;
    private int price;
    private int totalPrice; // 수량 *가격
    private String productImge;
    private String productDesc;

    @Override
    public String toString() {
        return "OrderProduct{" +
                "productId=" + productId +
                ", qty=" + qty +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", productImge='" + productImge + '\'' +
                ", productDesc='" + productDesc + '\'' +
                '}';
    }
}
