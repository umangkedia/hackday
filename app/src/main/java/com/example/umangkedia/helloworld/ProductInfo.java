package com.example.umangkedia.helloworld;

import java.util.Map;

public class ProductInfo {

	String productId;
	
	String mainTitle;
	
	String sellingPrice;
	
	String primaryImageId;
	
	ProductAvailabilityDetails availabilityDetails;
	
	Map<String, Map<String, ImageData>> productMultipleImage;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getPrimaryImageId() {
		return primaryImageId;
	}

	public void setPrimaryImageId(String primaryImageId) {
		this.primaryImageId = primaryImageId;
	}

	public Map<String, Map<String, ImageData>> getProductMultipleImage() {
		return productMultipleImage;
	}

	public void setProductMultipleImage(
			Map<String, Map<String, ImageData>> productMultipleImage) {
		this.productMultipleImage = productMultipleImage;
	}

	public ProductAvailabilityDetails getAvailabilityDetails() {
		return availabilityDetails;
	}

	public void setAvailabilityDetails(
			ProductAvailabilityDetails availabilityDetails) {
		this.availabilityDetails = availabilityDetails;
	}
	
}
