package com.sku.fitizen.domain.store;
import lombok.Getter;
import java.util.List;
import java.sql.Date;

@Getter
public class Order {

    private int orderId ;
    private String userId;
    private String impUid;
    private String merchantUid;
    private int totalPrice;
    private Date paidAt;
    private int usePoint;
    private List<OrderProduct> orderProductList;

}
