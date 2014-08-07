package com.example.umangkedia.helloworld;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

import com.android.volley.NetworkResponse;

public class ResponseUtils {

	public static Reader getJsonReader(NetworkResponse response){
		Reader jsonReader = null;

		try{
			boolean isGZipped = isResponseGZipped(response);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(response.data);
			if(!isGZipped){
				jsonReader = new InputStreamReader(inputStream);
			}
			else{
				jsonReader = new InputStreamReader(new GZIPInputStream(inputStream));
			}
		}
		catch(Exception e){
		}
		return jsonReader;
	}

	public static boolean isResponseGZipped(NetworkResponse response){
		boolean isGZipped = false;
		String contentResponse = response.headers.get("Content-Encoding");
		if(contentResponse != null && contentResponse.length() != 0 && contentResponse.contains("gzip")){
			isGZipped = true;
		}
		return isGZipped;
	}

	
}
