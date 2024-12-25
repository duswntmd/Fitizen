package com.sku.fitizen.mapper.store;

import com.sku.fitizen.domain.store.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    CartItem findCartItemByUserIdAndProductId(int productId, String userId); // 상품 조회

    // 장바구니 항목 추가
    void insertCartItem(CartItem cartItem);

    // 특정 사용자의 장바구니 항목 조회
    List<CartItem> selectCartItemsByUserId(@Param("user_id") String userId);

    //수량변경
    void updateQty(CartItem cartItem);

    // 장바구니 항목 삭제
    void deleteCartItemsByUserId(CartItem cartItem);

    List<CartItem> findSelectedCartItems(String user_id,List<Integer> product_id);

}
