package com.sku.fitizen.controller.challenge;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    PortOneService portOneService;


    private final IamportClient iamportClient;



    public PaymentController() {
       /* this.iamportClient = new IamportClient("REST_API_KEY",
                "REST_API_SECRET");

        */
        this.iamportClient = new IamportClient("1503384544352203",
                "oT90x1wAb37Xj64cfvnZEmMFA7GUqmQPAvuc1vgShf4Iab5ZWHyYB2Kq4WxAM9S5RDG1sQyGHJ1UxsJP");
    }

    @ResponseBody
    @RequestMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        // 결제 검증
        return iamportClient.paymentByImpUid(imp_uid);

        /*
        imp_uid (아임포트 결제 고유 ID) ,merchant_uid (가맹점 주문 고유 ID)
        imp_uid: 아임포트가 결제 시 자동으로 생성하는 결제 고유 ID. 아임포트의 결제 데이터를 조회할 때 사용.
        merchant_uid: 가맹점이 주문을 구분하기 위해 직접 생성하는 주문 고유 ID. 가맹점 내부적으로 주문 관리에 사용.
        */
    }


    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelPayment(@RequestBody Map<String, Object> cancelRequest) {
        // 클라이언트에서 넘어온 데이터
        String merchantUid = (String) cancelRequest.get("merchant_uid");
        int cancelAmount = (int) cancelRequest.get("cancel_request_amount");
        String reason = (String) cancelRequest.get("reason");

        // 포트원 토큰 발급
        String token = portOneService.getPortOneToken();

        // 결제 취소 요청
        Map<String, Object> cancelResponse = portOneService.cancelPayment(token, merchantUid, cancelAmount, reason);

        // 응답 결과 반환
        return ResponseEntity.ok(cancelResponse);

    }




}