package com.sku.fitizen.controller.challenge;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    PaymentService paymentService;



    private final IamportClient iamportClient;


    @Autowired
    public PaymentController(@Value("${REST_API_KEY}") String restApiKey,
                             @Value("${REST_API_SECRET}") String restApiSecret) {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    }

    @GetMapping("/pay")
    public String pay(@SessionAttribute (value = "user") User user , Model model )
    {
        model.addAttribute("user", user);
        return "th/user/pay";
    }


    @ResponseBody
    @RequestMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
     //   return iamportClient.paymentByImpUid(imp_uid);

        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
        System.out.println("Response from Iamport: " + response.getMessage()+"1"+response.getResponse()); // 디버깅을 위한 로그 출력
        return response;

        /*
        imp_uid (아임포트 결제 고유 ID) ,merchant_uid (가맹점 주문 고유 ID)
        imp_uid: 아임포트가 결제 시 자동으로 생성하는 결제 고유 ID. 아임포트의 결제 데이터를 조회할 때 사용.
        merchant_uid: 가맹점이 주문을 구분하기 위해 직접 생성하는 주문 고유 ID. 가맹점 내부적으로 주문 관리에 사용.
        */
    }

    @PostMapping("/savePayment")
    @ResponseBody
    public Map<String, Object> savePayment(@RequestBody com.sku.fitizen.domain.Payment payment)
    {
        Map<String, Object> response = new HashMap<>();

        boolean success  = paymentService.savePayment(payment);

        response.put("success", success);

        return response;
    }


    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelPayment(@RequestBody Map<String, Object> cancelRequest) {
        // 클라이언트에서 넘어온 데이터
        String merchantUid = (String) cancelRequest.get("merchant_uid");
        int cancelAmount = (int) cancelRequest.get("cancel_request_amount");
        String reason = (String) cancelRequest.get("reason");

        // 포트원 토큰 발급
        String token = paymentService.getPortOneToken();

        // 결제 취소 요청
        Map<String, Object> cancelResponse = paymentService.cancelPayment(token, merchantUid, cancelAmount, reason);

        // 응답 결과 반환
        return ResponseEntity.ok(cancelResponse);

    }


    @GetMapping("/getMyPayments/{userId}")
    public String getMyPayments(@SessionAttribute(value = "user") User user, @PathVariable("userId") String userId, Model model)
    {
        if (user == null || user.getId() == null) {
            return "redirect:/login/login";
        }

       List<com.sku.fitizen.domain.Payment> list =paymentService.getPaymentLsit(userId);
       model.addAttribute("payments", list);

       int balance =paymentService.getBalanceBYUserId(userId);
       model.addAttribute("balance", balance);
        return "th/user/myPayments";
    }


}