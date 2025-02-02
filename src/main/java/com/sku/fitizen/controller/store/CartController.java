package com.sku.fitizen.controller.store;

import com.sku.fitizen.domain.store.CartItem;
import com.sku.fitizen.domain.store.Product;
import com.sku.fitizen.service.PaymentService;
import com.sku.fitizen.service.store.CartService;
import com.sku.fitizen.service.store.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    PaymentService paymentService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ShopService shopService;

    @GetMapping("")
    public String cart(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        // DB에서 해당 사용자의 장바구니 목록을 가져옴
        List<CartItem> cart = cartService.selectCartItemsByUserId(userId);
        List<Product> products = new ArrayList<>();
        // 모든 상품의 prid 값을 출력
        for (CartItem item : cart) {
            int prid = item.getProduct().getPrid();
            Product product = shopService.getProductById(prid);
            item.setProduct(product);
        }

        int point=paymentService.getBalanceBYUserId(userId);
        model.addAttribute("point", point);
        model.addAttribute("cart", cart);  // 장바구니 항목을 모델에 추가
        model.addAttribute("userId", userId);
        return "shopCart";
    }

    // 수량 업데이트
    @PostMapping("/update")
    public String updateCartItem(@RequestParam("productId") int productId,
                                 @RequestParam("qty") int qty,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        CartItem cartItem= cartService.findCartItemByUserIdAndProductId(userId,productId);
        if (cartItem != null) {
            cartItem.setQty(qty);  // 수량 업데이트
            cartItem.setProduct_price(cartItem.getProduct_price());
            cartItem.setPrice(cartItem.getProduct_price()*qty);
            cartService.updateCartItem(cartItem);  // 서비스 호출하여 수량 업데이트
        }
        redirectAttributes.addFlashAttribute("message", "장바구니 수량이 업데이트되었습니다.");
        return "redirect:/cart";
    }

    // 장바구니 항목 삭제
    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam("productId") int productId,
                                HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String userId = (String) session.getAttribute("userId");
        cartService.deleteCartItem(productId, userId);
        redirectAttributes.addFlashAttribute("message", "상품이 장바구니에서 삭제되었습니다.");
        return "redirect:/cart";
    }

}
