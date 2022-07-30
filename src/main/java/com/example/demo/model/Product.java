package com.example.demo.model;

import com.example.demo.model.ProductCategory;

public record Product(String title, ProductCategory productCategory, Float price, String serialId){}

