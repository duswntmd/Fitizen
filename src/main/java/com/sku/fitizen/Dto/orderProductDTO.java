package com.sku.fitizen.Dto;


import com.sku.fitizen.domain.store.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class orderProductDTO {

  private int orderId;
  private  String userId;
  private  String impUid;
  private  String merchantUid;
  private  int totalPrice;
  private Date paidAt; // 결제 날짜
  private List<CartItem> orderProducts ;

  @Override
  public String toString() {
    return "orderProductDTO{" +
            "orderId=" + orderId +
            ", userId='" + userId + '\'' +
            ", impUid='" + impUid + '\'' +
            ", merchantUid='" + merchantUid + '\'' +
            ", totalPrice=" + totalPrice +
            ", paidAt=" + paidAt +
            ", orderProducts=" + orderProducts +
            '}';
  }
}
