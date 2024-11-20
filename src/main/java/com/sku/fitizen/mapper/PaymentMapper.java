package com.sku.fitizen.mapper;

import org.apache.ibatis.annotations.Param;
import com.sku.fitizen.Dto.orderProductDTO;
import com.sku.fitizen.domain.pay.Payment;
import com.sku.fitizen.domain.SpendingPoint;
import com.sku.fitizen.domain.store.CartItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {

  // 상품 결제 저장 (fitizen store)
   int insertOrder(orderProductDTO dto);

  int insertOrderProduct(@Param("product") CartItem product, @Param("orderId") int orderId);

   // 챌린지
   // 결제 기록 저장
   int  savePayment(Payment payment);

   // 결제 기록 (개인) 불러오기
   List<Payment> getPaymentList(String userId);

   // 개인 보유 잔여 포인트 목록 조회
   int getBalanceByUserId(String userId);

   // 사용 포인트 기록
   void saveSpendingPoint(SpendingPoint spendingPoint);

}
