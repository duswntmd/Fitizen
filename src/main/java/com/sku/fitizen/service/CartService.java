package com.sku.fitizen.service;

import com.sku.fitizen.domain.CartItem;
import com.sku.fitizen.domain.Order;
import com.sku.fitizen.domain.Product;
import com.sku.fitizen.mapper.CartMapper;
import com.sku.fitizen.mapper.OrderMapper;
import com.sku.fitizen.mapper.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private OrderMapper orderMapper;

    public List<CartItem> selectCartItemsByUserId(String userId) {

        return cartMapper.selectCartItemsByUserId(userId);
    }

    public void insertCartItem(CartItem cartItem) {
        cartMapper.insertCartItem(cartItem);
    }

    public CartItem findCartItemByUserIdAndProductId(String userId, int product_id) {
        return cartMapper.findCartItemByUserIdAndProductId(product_id, userId);
    }

    public void updateCartItem(CartItem cartItem) {

        cartMapper.updateQty(cartItem);
    }

    public Product getProductById(int productId) {
        return shopMapper.getProductById(productId);  // 상품 조회
    }

    public void deleteCartItem(int productId, String userId) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct_id(productId);
        cartItem.setUser_id(userId);
        cartMapper.deleteCartItemsByUserId(cartItem);
    }


}
