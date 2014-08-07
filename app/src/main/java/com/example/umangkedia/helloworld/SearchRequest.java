package com.example.umangkedia.helloworld;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.mygson.Gson;
import com.google.mygson.GsonBuilder;

public class SearchRequest extends Request<ObjectResponse>{

	private final Listener<ObjectResponse> listner;
	
	private Gson gson;
	
	public SearchRequest(int method, String url, Listener<ObjectResponse> listner, ErrorListener errorListener){
		super(method, url, errorListener);
		this.listner = listner;
		GsonBuilder gsonBuilder = new GsonBuilder();
		gson = gsonBuilder.create();
	}

	@Override
	protected Response<ObjectResponse> parseNetworkResponse(NetworkResponse response) {
		Log.d("SearchActivity", "result reveived");
		try{
			ObjectResponse objectResponse = gson.fromJson(ResponseUtils.getJsonReader(response), ObjectResponse.class);
			return Response.success(objectResponse, HttpHeaderParser.parseCacheHeaders(response));
		}
		catch(Exception e){
			Log.d("SearchActivity", "Exception while parsing");
			return Response.error(new VolleyError(response));
		}
	}
	
	
	@Override
	protected void deliverResponse(ObjectResponse response) {
		listner.onResponse(response);
	}
	
}
