package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopMapper {


   List<Product> getAllProducts();

   Product getProductById(int prid);

   boolean insertProduct(Product product);

   boolean updateProduct(Product product);

   boolean deleteProduct(int prid);

   int getPridByProductName(String prname);
}
