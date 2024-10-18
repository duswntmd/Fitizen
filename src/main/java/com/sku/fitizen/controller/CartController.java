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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ShopService shopService;

    @GetMapping("")
    public String cart(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        // DB에서 해당 사용자의 장바구니 목록을 가져옴
        List<CartItem> cart = cartService.selectCartItemsByUserId(userId);

// 모든 상품의 prid 값을 출력
        for (CartItem item : cart) {
            int prid = item.getProduct().getPrid();
            System.out.println("상품 ID: " + prid);
            Product product = shopService.getProductById(prid);
        }

        model.addAttribute("cart", cart);  // 장바구니 항목을 모델에 추가
        model.addAttribute("userId", userId);
        return "shopCart";
    }

//    @GetMapping("/update")
//    @ResponseBody
//    public Map<String,Boolean> updateCart(Model model, HttpSession session) {
//
//    }
//
//    @GetMapping("/delete")
//    @ResponseBody
//    public Map<String, Boolean> deleteCart(HttpSession session) {
//        String userId = (String) session.getAttribute("userId");
//    }

}
