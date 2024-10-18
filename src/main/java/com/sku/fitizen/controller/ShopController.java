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

    @GetMapping("/cart/add/{prid}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@PathVariable int prid, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String userId = (String) session.getAttribute("userId");

        // 로그인 여부 확인
        if (userId == null) {
            response.put("status", "error");
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // JSON 응답으로 반환
        }

        // 세션에서 장바구니 조회
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // 상품 정보 조회
        Product product = shopService.getProductById(prid);
        if (product != null) {
            // 장바구니에 이미 존재하는지 확인
            CartItem existingCartItem = null;
            for (CartItem item : cart) {
                if (item.getProduct().getPrid() == prid) {
                    existingCartItem = item;
                    break;
                }
            }

            if (existingCartItem != null) {
                // 이미 존재하는 경우 수량 증가
                existingCartItem.setQty(existingCartItem.getQty() + 1);
            } else {
                // 존재하지 않는 경우 새로 추가
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);  // 상품 정보 설정
                cartItem.setQty(1);  // 기본 수량을 1로 설정
                cartItem.setPrice(product.getPrprice());  // 상품 가격 설정
                cartItem.setUser_id(userId);  // 사용자 ID 설정
                cart.add(cartItem);

                // 데이터베이스에 장바구니 항목 추가
                cartService.insertCartItem(cartItem);
            }

            session.setAttribute("cart", cart);  // 세션에 장바구니 업데이트
            response.put("cartadded", true);
        } else {
            response.put("cartadded", false);
        }

        // JSON 응답으로 반환
        return ResponseEntity.ok().body(response);
    }


}
