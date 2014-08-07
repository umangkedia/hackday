package com.example.umangkedia.helloworld;

import com.google.mygson.annotations.SerializedName;

public class ImageData {
	
	@SerializedName("s3_path")
	String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
