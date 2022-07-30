package com.example.demo.controller;

import com.example.demo.service.CampaignManagementService;
import com.example.demo.model.Product;
import com.example.demo.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ad")
public class AdsController {

    @Autowired
    private CampaignManagementService campaignManagementService;

    @GetMapping(produces = "application/json")
    public Product serveAd(@RequestParam(name = "category") ProductCategory productCategory){
        return campaignManagementService.getHighestBidProduct(productCategory);
    }
}
