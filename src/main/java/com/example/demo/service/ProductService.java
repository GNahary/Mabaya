package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private final Map<String, Product> productMap = new HashMap<>();

    public void addProduct(Product product){
        productMap.put(product.serialId(), product);
    }

    public Product getProduct(String productSerial){
        return productMap.get(productSerial);
    }

    public List<Product> getAllProducts(){
        return List.copyOf(productMap.values());
    }
}
