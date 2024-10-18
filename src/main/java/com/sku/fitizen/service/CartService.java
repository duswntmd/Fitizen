package com.sku.fitizen.service;

import com.sku.fitizen.domain.CartItem;
import com.sku.fitizen.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    public List<CartItem> selectCartItemsByUserId(String userId) {
        return cartMapper.selectCartItemsByUserId(userId);
    }

    public int insertCartItem(CartItem cartItem) {
        // 실제 삽입된 행의 개수를 반환합니다.
        return cartMapper.insertCartItem(cartItem);
    }

    public void updateQty(CartItem cartItem) {
        cartMapper.updateQty(cartItem);
    }

    public void deleteCartItemsByUserId(CartItem cartItem) {
        cartMapper.deleteCartItemsByUserId(cartItem);
    }
}
