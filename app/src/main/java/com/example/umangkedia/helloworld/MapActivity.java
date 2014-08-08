package com.example.umangkedia.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MapActivity extends Activity implements GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private GoogleMap googleMap;

    //Flipkart's approx location
    double currentLatitude = 12.9539974;
    double currentLongitude = 77.6309395;

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    Button setButton;
    Button searchButton;
    MarkerOptions marker;
    EditText searchBox;

    // Creating HTTP client
    HttpClient httpClient;
    public static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyBrzWx3eYxgjWD8qywQW3TO4QHpnvpfvMg&";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        httpClient = new DefaultHttpClient();

        setButton = (Button) findViewById(R.id.setLocation);
        setButton.setOnClickListener(this);

        searchButton = (Button) findViewById(R.id.searchLocation);
        searchButton.setOnClickListener(this);

        searchBox = (EditText) findViewById(R.id.searchText);

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
        marker = new MarkerOptions()
                .position(new LatLng(currentLatitude, currentLongitude))
                .title("Hello Flipsters")
                .draggable(true);

        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Marker newMarker = googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(currentLatitude, currentLongitude)).zoom(10).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnMarkerDragListener(this);
        onMarkerDragEnd(newMarker);

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

        Log.d("MapActivity: Longitude and latitude", String.valueOf(position.latitude) + String.valueOf(position.longitude));
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
//            String searchBox = "address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=true_or_false";
            String searchText = null;
            try {
                searchText = URLEncoder.encode(searchBox.getText().toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (searchText.trim() != "")
                doSearch(GEOCODE_URL + "address=" + searchText + "&sensor=true_or_false");

            else
                Toast.makeText(MapActivity.this, "Enter something in search box",
                        Toast.LENGTH_SHORT).show();
        }
    }

    private void doSearch(String searchText) {

        DownloadTask downloadTask = new DownloadTask(this) {
            @Override
            public void receiveData(double latitude, double longitude) {
                currentLatitude = latitude;
                currentLongitude = longitude;
                if (marker != null)
                    googleMap.clear();

                LatLng latLng = new LatLng(latitude, longitude);
                marker = new MarkerOptions().position(latLng);
                Marker newMarker = googleMap.addMarker(marker);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                onMarkerDragEnd(newMarker);

            }
        };
        downloadTask.execute(GEOCODE_URL + searchText);
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
