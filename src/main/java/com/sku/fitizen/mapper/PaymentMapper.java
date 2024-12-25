package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.challenge.Rewards;
import com.sku.fitizen.domain.store.Order;
import org.apache.ibatis.annotations.Param;
import com.sku.fitizen.Dto.orderProductDTO;
import com.sku.fitizen.domain.pay.Payment;
import com.sku.fitizen.domain.pay.SpendingPoint;
import com.sku.fitizen.domain.store.CartItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {

  // 상품 결제 저장 (fitizen store)
   int insertOrder(orderProductDTO dto);
   int insertOrderProduct(@Param("product") CartItem product, @Param("orderId") int orderId);

   // 결제 기록 저장
   int  savePayment(Payment payment);
   // 결제 기록 (개인) 불러오기
   List<Payment> getPaymentList(String userId);
   // 포인트 사용 내역 불러오기
   List<SpendingPoint> getSpendingPointList(String userId);
   // 결제 상품  구매목록
   List<Order> getOrderProductsByUserId(@Param("userId") String userId, @Param("no") int no);

   void cancelOrder(@Param("impUid") String impUid, @Param("merchantUid") String merchantUid);
   // 개인 보유 잔여 포인트 목록 조회
   int getBalanceByUserId(String userId);
   // 지급 된 포인트 내역
  List<Rewards> myRewards(String userId);
   // 사용 포인트 기록
   void saveSpendingPoint(SpendingPoint spendingPoint);

}
