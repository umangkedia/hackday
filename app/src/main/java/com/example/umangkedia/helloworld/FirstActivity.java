package com.example.umangkedia.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.Map;


public class FirstActivity extends Activity {

    private EditText searchEditText;
    private  Button searchButton;

    private RequestQueue mRequestQueue = null;
    private final String BASE_URL = "http://mobileapi.flipkart.net/2/discover/getSearch?store=search.flipkart.com&start=0&count=10&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mRequestQueue = Volley.newRequestQueue(this);
        searchEditText = (EditText) findViewById(R.id.searchText);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Searching for " + searchEditText.getText(), Toast.LENGTH_SHORT).show();
                doSearch(searchEditText.getText().toString());
            }
        });
    }

    private void doSearch(String searchText) {
        try {
            String encodedURL = URLEncoder.encode(searchText, "UTF-8");
            doSearchRequest(encodedURL);
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doSearchRequest(String encodedURL) {
        SearchRequest searchRequest = new SearchRequest(Request.Method.GET, BASE_URL + encodedURL, new Response.Listener<ObjectResponse>() {
            @Override
            public void onResponse(ObjectResponse objectResponse) {
                Log.d("UmangLog", "Success");
                Map<String, ProductInfo> products = objectResponse.getResponse().getProduct();
                Log.d("UmangLog", "" + products.size());

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("UmangLog", "Error");

                    }
                }
        );
        mRequestQueue.add(searchRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
