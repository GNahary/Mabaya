package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.ProductCategory;
import com.example.demo.service.ProductService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(path = "/init")
    public List<Product> init(){
        for(int i = 0; i<100; i++){
            Product p = new Product("Product number " + i, getRandomCategory(), 1.0f, RandomStringUtils.random(5,true,true));
            productService.addProduct(p);
        }

        return productService.getAllProducts();
    }

    private ProductCategory getRandomCategory(){
        int bound = ProductCategory.values().length;
        Random random = new Random();
        int ordinal = random.nextInt(bound);
        return ProductCategory.values()[ordinal];
    }

}
