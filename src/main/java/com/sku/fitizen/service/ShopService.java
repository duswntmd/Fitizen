package com.sku.fitizen.service;

import com.sku.fitizen.domain.Product;
import com.sku.fitizen.mapper.ShopMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ShopMapper shopMapper;

    public ShopService(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    public List<Product> getAllProducts() {
        return shopMapper.getAllProducts();
    }

    public Product getProductById(int prid) {
        return shopMapper.getProductById(prid);
    }

    public void addProduct(Product product) {
        shopMapper.insertProduct(product);
    }

    public void updateProduct(Product product) {
        shopMapper.updateProduct(product);
    }

    public void deleteProduct(int prid) {
        shopMapper.deleteProduct(prid);
    }
    public void getpridByprname(String prname) {
        shopMapper.getPridByProductName(prname);
    }
}
