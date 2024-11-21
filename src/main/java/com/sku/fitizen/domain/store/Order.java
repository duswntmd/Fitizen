package com.sku.fitizen.domain.store;
import lombok.Getter;
import java.util.List;
import java.sql.Date;

@Getter
public class Order {
    private int orderId ;
    private String userId;
    private String merchantUid;
    private int totalPrice;
    private Date paidAt;
    private List<OrderProduct> orderProductList;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId='" + userId + '\'' +
                ", merchantUid='" + merchantUid + '\'' +
                ", totalPrice=" + totalPrice +
                ", paidAt=" + paidAt +
                ", orderProductList=" + orderProductList +
                '}';
    }
}
