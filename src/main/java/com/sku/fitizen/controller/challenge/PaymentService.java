package com.sku.fitizen.controller.challenge;

import com.sku.fitizen.domain.Payment;
import com.sku.fitizen.mapper.PaymentMapper;
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


    private static final String API_KEY = "1503384544352203";
    private static final String API_SECRET = "oT90x1wAb37Xj64cfvnZEmMFA7GUqmQPAvuc1vgShf4Iab5ZWHyYB2Kq4WxAM9S5RDG1sQyGHJ1UxsJP";



    // 결제 기록 저장
    public  boolean savePayment(Payment payment)
    {

        int result =paymentMapper.savePayment(payment);

        if(result == 1) return true;
        return  false;
    }

    public List<Payment> getPaymentLsit(String userId)
    {
        List<Payment> Payments =paymentMapper.getPaymentList(userId);

        return Payments;

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
