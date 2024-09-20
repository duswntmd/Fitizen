package com.sku.fitizen.mapper;


import com.sku.fitizen.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {


   // 결제 기록 저장
   int  savePayment(Payment payment);

   // 결제 기록 (개인) 불러오기
   List<Payment> getPaymentList(String userId);

   // 개인 보유 잔여 포인트 목록 조회
   int getBalanceByUserId(String userId);


}
