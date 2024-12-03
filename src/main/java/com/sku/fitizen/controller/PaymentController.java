package com.sku.fitizen.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.sku.fitizen.Dto.orderProductDTO;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.challenge.Rewards;
import com.sku.fitizen.domain.pay.SpendingPoint;
import com.sku.fitizen.domain.store.Order;
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
       // System.out.println("Response from Iamport: " + response.getMessage()+"1"+response.getResponse()); // 디버깅을 위한 로그 출력
        return response;

        /*
        imp_uid (아임포트 결제 고유 ID) ,merchant_uid (가맹점 주문 고유 ID)
        imp_uid: 아임포트가 결제 시 자동으로 생성하는 결제 고유 ID. 아임포트의 결제 데이터를 조회할 때 사용.
        merchant_uid: 가맹점이 주문을 구분하기 위해 직접 생성하는 주문 고유 ID. 가맹점 내부적으로 주문 관리에 사용.
        */
    }

    @PostMapping("/savePayment")
    @ResponseBody
    public Map<String, Object> savePayment(@RequestBody com.sku.fitizen.domain.pay.Payment payment)
    {
        Map<String, Object> response = new HashMap<>();

        boolean success  = paymentService.savePayment(payment);

        response.put("success", success);

        return response;
    }

    @PostMapping("/orderPayment")
    @ResponseBody
    public Map<String, Object> orderPayment(@RequestBody orderProductDTO dto)
    {
        Map<String, Object> response = new HashMap<>();
        boolean success  = paymentService.saveOrderPayment(dto);
        response.put("success", success);
        return response;
    }



    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelPayment(@SessionAttribute("user") User user,@RequestBody Map<String, Object> cancelRequest) {
        // 클라이언트에서 넘어온 데이터
        String impUid = (String) cancelRequest.get("imp_uid");
        String merchantUid = (String) cancelRequest.get("merchant_uid");
        int cancelAmount = (int) cancelRequest.get("cancel_request_amount");
        String reason = (String) cancelRequest.get("reason");
        int usePoint =(int)  cancelRequest.get("cancel_point");
        // 포트원 토큰 발급
        String token = paymentService.getPortOneToken();

        // 결제 취소 요청
        Map<String, Object> response = paymentService.cancelPayment(user,usePoint,token,impUid ,merchantUid, cancelAmount, reason);

        // 응답 결과 반환
        return ResponseEntity.ok(response);

    }


    @GetMapping("/getMyPayments")
    public String getMyPayments(@SessionAttribute(value = "user") User user,@RequestParam(value = "categoryId",defaultValue ="1") int categoryId ,Model model)
    {
       if(categoryId == 2) {
           List<com.sku.fitizen.domain.pay.Payment> list = paymentService.getPaymentList(user.getId());
           model.addAttribute("payments", list);
       }
        model.addAttribute("user", user);
       int balance =paymentService.getBalanceBYUserId(user.getId());
       model.addAttribute("balance", balance);
        return "th/user/myPayments";
    }


    @GetMapping("/getMyPaymentsData")
    @ResponseBody
    public Map<String, Object> getMyPaymentsData(
            @SessionAttribute(value = "user") User user,
            @RequestParam(value = "categoryId", defaultValue = "2") int categoryId) {
        Map<String, Object> response = new HashMap<>();
        if (categoryId == 2) {
            // 충전 내역 데이터
            List<com.sku.fitizen.domain.pay.Payment> payments = paymentService.getPaymentList(user.getId());
            response.put("payments", payments);
        } else if (categoryId == 3) {
            // 사용 내역 데이터
            List<SpendingPoint> spendingPoints = paymentService.getSpendingPointList(user.getId());
            response.put("spendingPoints", spendingPoints);
        } else if (categoryId==1) {
            List<Rewards> myRewards= paymentService.myRewards(user.getId());
            response.put("myRewards", myRewards);
        }
        return response;
    }


    @GetMapping("/myOrder")
    public String getMyOrder(@SessionAttribute(value = "user") User user, Model model)
    {

        List<Order> myOrders =paymentService.getOrderProductsByUserId(user.getId(),0);
        List<Order> myCancelledOrders =paymentService.getOrderProductsByUserId(user.getId(),1);
        model.addAttribute("orders", myOrders);
        model.addAttribute("cancellOrders", myCancelledOrders);
        return "th/user/myOrder";
    }

}