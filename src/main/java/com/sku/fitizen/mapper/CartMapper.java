package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    // 장바구니 항목 추가
    int insertCartItem(CartItem cartItem);

    // 특정 사용자의 장바구니 항목 조회
    List<CartItem> selectCartItemsByUserId(@Param("user_id") String userId);

    //수량변경
    void updateQty(CartItem cartItem);

    // 장바구니 항목 삭제
    void deleteCartItemsByUserId(CartItem cartItem);
}
