package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.CartItem;
import com.sku.fitizen.domain.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
   List<CartItem> insertOrder(Order order);

}

