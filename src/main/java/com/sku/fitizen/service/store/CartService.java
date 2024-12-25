package com.sku.fitizen.service.store;

import com.sku.fitizen.domain.store.CartItem;
import com.sku.fitizen.domain.store.Product;
import com.sku.fitizen.mapper.store.CartMapper;
import com.sku.fitizen.mapper.store.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ShopMapper shopMapper;

    public List<CartItem> selectCartItemsByUserId(String userId) {


        return cartMapper.selectCartItemsByUserId(userId);
    }

    public void insertCartItem(CartItem cartItem) {

        // System.out.println(cartItem);
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
