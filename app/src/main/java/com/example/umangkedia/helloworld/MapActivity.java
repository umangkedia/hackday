package com.example.umangkedia.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends Activity implements GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private GoogleMap googleMap;

    //Flipkart's approx location
    double currentLatitude = 12.9539974;
    double currentLongitude = 77.6309395;

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    Button setButton;
    Button searchButton;

    //Using volley .. Yay!!
    private RequestQueue mRequestQueue = null;

    // Creating HTTP client
    HttpClient httpClient;
    public static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mRequestQueue = Volley.newRequestQueue(this);

        httpClient = new DefaultHttpClient();

        setButton = (Button) findViewById(R.id.setLocation);
        setButton.setOnClickListener(this);

        searchButton = (Button) findViewById(R.id.searchLocation);
        searchButton.setOnClickListener(this);

        try {
            // Loading map
            initializeMap();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            else
                initializeMarker();
        }
    }

    private void initializeMarker() {
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(currentLatitude, currentLongitude))
                .title("Hello Flipsters")
                .draggable(true);

        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(currentLatitude, currentLongitude)).zoom(10).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnMarkerDragListener(this);

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d("Mapactivity" , "Dragging Marker start");
        //TODO implement this
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //TODO implement this
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position = marker.getPosition(); //
        Toast.makeText(MapActivity.this, "[MapActivity]Lat " + position.latitude + " "  + "Long " + position.longitude,
                Toast.LENGTH_SHORT).show();

        Intent intentMessage = new Intent();

        intentMessage.putExtra(LATITUDE, position.latitude);
        intentMessage.putExtra(LONGITUDE, position.longitude);
        setResult(1, intentMessage);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setLocation) {
            Log.d("MapActivity" , "Ending activity");
            finish();
        }
        else if (view.getId() == R.id.searchLocation) {
            doSearch();
        }
    }

    private void doSearch() {
        String searchText = "address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=true_or_false";
        try {
            String encodedURL = URLEncoder.encode(searchText, "UTF-8");
            doSearchRequest(encodedURL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void doSearchRequest(String encodedURL) {
        /*
        SearchRequest searchRequest = new SearchRequest(Request.Method.GET, GEOCODE_URL + encodedURL, new Response.Listener<ObjectResponse>() {
            @Override
            public void onResponse(ObjectResponse objectResponse) {
                Log.d("MapActivity", objectResponse.toString());
                List<Object> results = objectResponse.getResponse().getResults();
                Map<String, Object> result = (HashMap<String, Object>)results.get(0);
                Map<String, Object> geometry = (Map<String, Object>) result.get("geometry");
                Map<String, Object> location = (Map<String, Object>) geometry.get("location");

                Log.d("MapActivity", (String)location.get("lat"));


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("MapActivity", "Error");

                    }
                }
        );
        mRequestQueue.add(searchRequest);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
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
