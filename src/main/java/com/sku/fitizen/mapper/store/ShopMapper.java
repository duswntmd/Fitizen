package com.sku.fitizen.mapper.store;

import com.sku.fitizen.domain.store.Product;
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
