package com.sku.fitizen.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.AccessToken;
import com.sku.fitizen.Dto.orderProductDTO;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.challenge.Rewards;
import com.sku.fitizen.domain.pay.Payment;
import com.sku.fitizen.domain.pay.SpendingPoint;
import com.sku.fitizen.domain.store.CartItem;
import com.sku.fitizen.domain.store.Order;
import com.sku.fitizen.mapper.PaymentMapper;
import com.sku.fitizen.mapper.store.CartMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    CartMapper cartMapper;


    private final IamportClient iamportClient;

    // 생성자에서 API Key와 Secret을 주입
    @Autowired
    public PaymentService(@Value("${REST_API_KEY}") String restApiKey,
                          @Value("${REST_API_SECRET}") String restApiSecret) {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    }


    // 결제 기록 저장 ( fitizen store)
    @Transactional
    public  boolean saveOrderPayment (orderProductDTO dto)
    {

        // 결제 정보  오더 테이블, 주문 상품들 테이블 , 장바구니 제거 3젝션

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
            paymentMapper.saveSpendingPoint(new SpendingPoint(dto.getUserId(),dto.getAppliedPoints(),0,"상점결제"));
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
    public List<Payment> getPaymentList(String userId) {return paymentMapper.getPaymentList(userId);}
    // 포인트 사용 내역 불러오기
    public List<SpendingPoint> getSpendingPointList(String userId) {return paymentMapper.getSpendingPointList(userId);}
    // 잔액 포인트 불러오기
    public  int  getBalanceBYUserId(String userId) {return paymentMapper.getBalanceByUserId(userId);}
    // 지급된 포인트 내역
    public  List<Rewards> myRewards(String userId){return paymentMapper.myRewards(userId);}

    // 유저 결제 상품 목록
    public List<Order> getOrderProductsByUserId(String userId, int no){ return paymentMapper.getOrderProductsByUserId(userId ,no);}

    // 포트원 API 토큰 발급
    public String getPortOneToken() {
        try {
            // 토큰 발급 요청
            AccessToken response = iamportClient.getAuth().getResponse();
           // System.out.println("발급된 토큰: " + response.getToken());
            return response.getToken();
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException("IAMPORT 토큰 발급 실패", e);
        }

    }


    // 결제 취소 요청 메서드
    public Map<String, Object> cancelPayment(User user, int usePoint, String token, String impUid, String merchantUid, int cancelAmount, String reason) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // 발급받은 토큰을 헤더에 추가
        headers.setContentType(MediaType.APPLICATION_JSON);
        // System.out.println(impUid+"/"+merchantUid+"/"+cancelAmount);
        // 요청 데이터 구성
        Map<String, Object> body = new HashMap<>();
        body.put("imp_uid", impUid);
        body.put("merchant_uid", merchantUid); // 취소할 주문 번호
        body.put("cancel_request_amount", cancelAmount); // 취소할 금액
        body.put("reason", reason); // 취소 사유
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        // 헤더 출력
        //System.out.println("Headers: " + entity.getHeaders());
        // 바디 출력
        //System.out.println("Body: " + entity.getBody());
        // 포트원 결제 취소 API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity("https://api.iamport.kr/payments/cancel", entity, Map.class);
       // System.out.println("!!!!"+response.getBody());
        Integer code = (Integer)response.getBody().get("code");
        if (code ==0)
        {
            paymentMapper.cancelOrder(impUid,merchantUid);
            paymentMapper.saveSpendingPoint(new SpendingPoint(user.getId(),usePoint,1,"상점 결제포인트"));

        }

        return response.getBody(); // 응답 데이터를 반환
    }
}
