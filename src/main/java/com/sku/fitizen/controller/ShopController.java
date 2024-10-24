package com.sku.fitizen.controller;

import com.sku.fitizen.domain.CartItem;
import com.sku.fitizen.domain.Product;
import com.sku.fitizen.service.CartService;
import com.sku.fitizen.service.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private CartService cartService;

    private  ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("")
    public String list(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("list",shopService.getAllProducts());
        return "shopList";
    }



    @GetMapping("/shopDetail/{prid}")
    public String showProductDetail(@PathVariable int prid, Model model, HttpSession session) {
        Product product = shopService.getProductById(prid);
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("product", product);
        model.addAttribute("userId", userId);
        return "shopDetail";
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam("prid") int productId, @RequestParam("userId") String userId,@RequestParam("qty") int qty, HttpSession session) {
        CartItem existingCartItem = cartService.findCartItemByUserIdAndProductId(userId, productId);
        Map<String, Object> response = new HashMap<>();

        System.out.println(productId+"    "+userId);
        // 로그인 여부 확인
        if (userId == null) {
            response.put("status", "error");
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // JSON 응답으로 반환
        }
        if (existingCartItem != null) {
            // 장바구니에 해당 상품이 있으면 수량 증가
            existingCartItem.setQty(existingCartItem.getQty() + 1);
            cartService.updateCartItem(existingCartItem);
            response.put("status", "success");
            response.put("message", "장바구니에 상품 수량이 업데이트되었습니다.");
        } else {
            // 장바구니에 없으면 새로 추가
            Product product = cartService.getProductById(productId);  // 상품 정보 조회
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct_id(productId);
            newCartItem.setUser_id(userId);
            newCartItem.setQty(qty);
            newCartItem.setProduct_price(product.getPrprice());  // 제품 가격 설정
            newCartItem.setPrice(product.getPrprice()* newCartItem.getQty());
            newCartItem.setProduct_name(product.getPrname());  // 제품명 설정
            cartService.insertCartItem(newCartItem);
            response.put("status", "success");
            response.put("message", "장바구니에 새 상품이 추가되었습니다.");

        }
        // 최종 응답 반환
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/cart/add")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam("prid") int prid,
//                                                         @RequestParam("qty") int qty,
//                                                         HttpSession session) {
//        Map<String, Object> response = new HashMap<>();
//        String userId = (String) session.getAttribute("userId");
//
//        // 로그인 여부 확인
//        if (userId == null) {
//            response.put("status", "error");
//            response.put("message", "로그인이 필요합니다.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // JSON 응답으로 반환
//        }
//
//        // 상품 정보 조회
//        Product product = shopService.getProductById(prid);
//        if (product == null) {
//            response.put("status", "error");
//            response.put("message", "해당 상품을 찾을 수 없습니다.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // 상품 없음 오류 반환
//        }
//
//        // CartItem 생성
//        CartItem cartItem = new CartItem();
//        cartItem.setProduct_id(prid);
//        cartItem.setQty(cartItem.getQty()+1);
//        cartItem.setPrice(product.getPrprice());  // 상품 가격 설정
//        cartItem.setUser_id(userId);
//        cartItem.setProduct(product);
//        cartItem.setProduct_name(product.getPrname());
//        cartItem.setProduct_price(product.getPrprice());
//        System.out.println(cartItem.getProduct());
//        // 장바구니에 추가 또는 수량 업데이트
//        cartService.addOrUpdateCartItem(cartItem);
//
//        // 세션에서 장바구니 상태 업데이트
//        List<CartItem> cart = cartService.selectCartItemsByUserId(userId);
//        session.setAttribute("cart", cart);  // 세션에 갱신된 장바구니 저장
//
//        response.put("status", "success");
//        response.put("message", "상품이 장바구니에 성공적으로 추가되었습니다.");
//        return ResponseEntity.ok(response);
//    }


}
