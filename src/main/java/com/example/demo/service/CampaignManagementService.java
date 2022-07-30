package com.example.demo.service;

import com.example.demo.model.Campaign;
import com.example.demo.model.Product;
import com.example.demo.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CampaignManagementService {

    @Value(value = "${active.campaign.duration}")
    private Integer ACTIVE_CAMPAIGN_DURATION;

    @Autowired
    ProductService productService;

    private final Map<ProductCategory, CategorizedCampaignedProductsSilo> categoryToCampaignedProductsMap = new HashMap<>();

    public void processNewCampaign(Campaign campaign){
        for (String productSerialNumber:campaign.campaignProducts()) {
            CampaignedProduct campaignedProduct = new CampaignedProduct(campaign.startDate(), campaign.bid(), productService.getProduct(productSerialNumber));
            processCampaignedProduct(campaignedProduct);
        }
    }

    public Product getHighestBidProduct(ProductCategory productCategory){
        return getHighestBidCampaignedProduct(productCategory).orElseGet(this::getHighestBidProductCategoryAgnostic);
    }

    private Optional<Product> getHighestBidCampaignedProduct(ProductCategory productCategory){
        CategorizedCampaignedProductsSilo categorizedCampaignedProductsSilo = categoryToCampaignedProductsMap.get(productCategory);
        if(categorizedCampaignedProductsSilo == null || categorizedCampaignedProductsSilo.getHighestBidCampaignedProduct() == null ){
            return Optional.empty();
        }
        return Optional.ofNullable(categorizedCampaignedProductsSilo.getHighestBidCampaignedProduct().product());
    }

    private Product getHighestBidProductCategoryAgnostic(){
        return categoryToCampaignedProductsMap.values().stream()
                .map(CategorizedCampaignedProductsSilo::getHighestBidCampaignedProduct)
                .max(Comparator.comparingDouble(CampaignedProduct::bid))
                .map(CampaignedProduct::product)
                .orElseThrow(); // If no products have valid campaign - throw an exception
    }

    private void processCampaignedProduct(CampaignedProduct campaignedProduct){
        ProductCategory productCategory = campaignedProduct.product().productCategory();
        CategorizedCampaignedProductsSilo campaignedProducts = categoryToCampaignedProductsMap.computeIfAbsent(productCategory, pc->new CategorizedCampaignedProductsSilo());
        campaignedProducts.addCategorizedCampaignedProduct(campaignedProduct);
    }


    private class CategorizedCampaignedProductsSilo {
        private CampaignedProduct highestBidCampaignedProduct;
        private final List<CampaignedProduct> categorizedCampaignedProducts = new ArrayList<>();
        public CategorizedCampaignedProductsSilo() {}

        public void addCategorizedCampaignedProduct(CampaignedProduct campaignedProduct){
            if(isActiveCampaign(campaignedProduct.startDate())){
                if(highestBidCampaignedProduct == null || highestBidCampaignedProduct.bid() < campaignedProduct.bid()){
                    highestBidCampaignedProduct = campaignedProduct;
                }
            }
            categorizedCampaignedProducts.add(campaignedProduct);
        }

        public CampaignedProduct getHighestBidCampaignedProduct() {
            if(!isActiveCampaign(highestBidCampaignedProduct.startDate())){
                updateCampaignedProductsSilo();
            }
            return highestBidCampaignedProduct;
        }

        private boolean isActiveCampaign(LocalDateTime campaignStartDate){
            return (hasCampaignStarted(campaignStartDate) && !hasCampaignEnded(campaignStartDate));
        }

        private boolean hasCampaignStarted(LocalDateTime campaignStartDate) {
            return campaignStartDate.isBefore(LocalDateTime.now()) || campaignStartDate.isEqual(LocalDateTime.now());
        }

        private boolean hasCampaignEnded(LocalDateTime campaignStartDate){
            return campaignStartDate.plusDays(ACTIVE_CAMPAIGN_DURATION).isBefore(LocalDateTime.now());
        }

        private void updateCampaignedProductsSilo(){
            categorizedCampaignedProducts.removeIf(cp->hasCampaignEnded(cp.startDate()));
            highestBidCampaignedProduct = categorizedCampaignedProducts.stream().max(Comparator.comparing(CampaignedProduct::bid)).orElse(null);
        }
    }

    private record CampaignedProduct(LocalDateTime startDate, Float bid, Product product){}

}
