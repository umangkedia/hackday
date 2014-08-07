package com.example.umangkedia.helloworld;

import com.google.mygson.annotations.SerializedName;

public class ProductAvailabilityDetails {

	@SerializedName("product.availability.status")
	String availability;

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}
	
}
