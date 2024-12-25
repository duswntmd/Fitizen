package com.sku.fitizen.service.store;

import com.sku.fitizen.domain.store.Product;
import com.sku.fitizen.mapper.store.ShopMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

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
    public String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

}
