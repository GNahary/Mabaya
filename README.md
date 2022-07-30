# Mabaya

This is the code for the assignment.
A couple of things to notice:
1. There are several aspects were ignored for brevity (and since they didn't appear in the criteria) - persistance, multithreading
2. The algorithm is based on the assumption that a product can be returned infinite number of time, as long as it has the heighest active bid. Another base assumption is that campaigns can be create at any time, and to start at the moment they are created. If this is untrue, several optimizations might be added. I've also tried providing best performance for serving ads, then for campaign additions. 
3. As strange as it may sound, I found no real reason to save a Campaign as-is. Two campaigns with the same startDate and bid that will include the same product will yield the same result as just one. There is no campaign maintenance so there is no real point in holding it. Classes such as CampaignedProduct and CategorizedCampaignedProductsSilo were declared as private inner classes since they are an implementation detail, as opposed to Campaign and Product.
4.  Initialization is done using the following call: POST localhost:8080/product/init
5. Adding new campaign is done using the following call: POST localhost:8080/campaign with a body of { "name":"campaign name", "startDate":"yyyy-MM-ddThh:mm", "bid": xx.xx, "campaignProducts":["serial 1", "serial 2"] ... }
6. Active campaign time was moved to properties file, since in cases of onPrem a customer might wish to change this.
7. We're using JDK 17 here.
