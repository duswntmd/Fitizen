package com.sku.fitizen.service;

import com.sku.fitizen.Dto.orderProductDTO;
import com.sku.fitizen.domain.pay.Payment;
import com.sku.fitizen.domain.store.CartItem;
import com.sku.fitizen.mapper.PaymentMapper;
import com.sku.fitizen.mapper.store.CartMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    CartMapper cartMapper;


    private static final String API_KEY = "1503384544352203";
    private static final String API_SECRET = "oT90x1wAb37Xj64cfvnZEmMFA7GUqmQPAvuc1vgShf4Iab5ZWHyYB2Kq4WxAM9S5RDG1sQyGHJ1UxsJP";



    // 결제 기록 저장 ( fitizen store)
    @Transactional
    public  boolean saveOrderPayment (orderProductDTO dto)
    {
        // 결제 정보  오더 테이블, 주문 상품들 테이블 , 장바구니 제거 3젝션
        // System.out.println(dto);
        paymentMapper.insertOrder(dto);
        int orderId =dto.getOrderId();
        // orderProduct 테이블에 저장
        boolean saved = dto.getOrderProducts().stream()
                .allMatch(product -> paymentMapper.insertOrderProduct(product, orderId) > 0);

        if (saved) {
            dto.getOrderProducts().forEach(product -> {
                CartItem cartItem = new CartItem();
                cartItem.setProduct_id(product.getProduct_id());
                cartItem.setUser_id(dto.getUserId());
                cartMapper.deleteCartItemsByUserId(cartItem); // 장바구니 항목 삭제
            });
            return true;
        }

        return false; // 실패 시 false 반환
    }


    // 결제 기록 저장
    public  boolean savePayment(Payment payment)
    {

        int result =paymentMapper.savePayment(payment);

        if(result == 1) return true;
        return  false;
    }

    // 결제 기록 불러오기
    public List<Payment> getPaymentLsit(String userId)
    {
        List<Payment> Payments =paymentMapper.getPaymentList(userId);

        return Payments;

    }


    // 잔액 포인트 불러오기
    public  int  getBalanceBYUserId(String userId)
    {
        return paymentMapper.getBalanceByUserId(userId);
    }





    // 포트원 API 토큰 발급
    public String getPortOneToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("imp_key", API_KEY);
        body.put("imp_secret", API_SECRET);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://api.iamport.kr/users/getToken", entity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        String token = (String) ((Map) responseBody.get("response")).get("access_token");

        return token;
    }


    // 결제 취소 요청 메서드
    public Map<String, Object> cancelPayment(String token, String merchantUid, int cancelAmount, String reason) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // 발급받은 토큰을 헤더에 추가
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 데이터 구성
        Map<String, Object> body = new HashMap<>();
        body.put("merchant_uid", merchantUid); // 취소할 주문 번호
        body.put("cancel_request_amount", cancelAmount); // 취소할 금액
        body.put("reason", reason); // 취소 사유

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // 포트원 결제 취소 API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity("https://api.iamport.kr/payments/cancel", entity, Map.class);

        return response.getBody(); // 응답 데이터를 반환
    }
}
