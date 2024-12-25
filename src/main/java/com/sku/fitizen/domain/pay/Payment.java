package com.sku.fitizen.domain.pay;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

  private  int  paymentId;  //고유 결제 아이디
  private  String  userId;
  private  String  impUid; //아임포트에서 제공하는 고유 결제 번호
  private  String  merchantUid; // 상점에서 생성한 고유 주문 번호
  private  int  paid_amount; // 결제 금액
  private  int  point;
  private Date paidAt; // 결제 날짜

}


