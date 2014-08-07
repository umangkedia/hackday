package com.example.umangkedia.helloworld;

import com.google.mygson.annotations.SerializedName;

public class ObjectResponse {

	@SerializedName("RESPONSE")
	SearchResponse response;

	public SearchResponse getResponse() {
		return response;
	}

	public void setResponse(SearchResponse response) {
		this.response = response;
	}
}
