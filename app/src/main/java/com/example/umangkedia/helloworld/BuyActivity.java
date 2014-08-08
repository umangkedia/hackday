package com.example.umangkedia.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BuyActivity extends Activity  implements View.OnClickListener{

    private GoogleMap googleMap;

    //Flipkart's approx location
    double currentLatitude = 12.9539974;
    double currentLongitude = 77.6309395;

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    Button accept;
    Button reject;

    private final String CHECK_POSITION_URL = "http://172.17.89.113:25500/shopping_item/check";
    private final String BUY_ITEM_URL = "http://172.17.89.113:25500//shopping_item/close";
    private final String GET_ITEMS_URL = "http://172.17.89.113:25500//shopping_item/fetch";
    private final String CREATE_GEO_FENCING_URL = "http://172.17.89.113:25500//shopping_item/create";

    public static String task_id;
    MarkerOptions marker;
    TextView question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        accept = (Button) findViewById(R.id.yes);
        accept.setOnClickListener(this);

        reject = (Button) findViewById(R.id.no);
        reject.setOnClickListener(this);

        question = (TextView) findViewById(R.id.question);

        try {
            // Loading map
            initializeMap();
            onNewIntent(getIntent());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNewIntent(Intent newIntent) {
        this.setIntent(newIntent);
        Log.d("BuyActivity", "Activity Launched through Notification");

        String message = getIntent().getStringExtra("question");
        task_id = getIntent().getStringExtra("task_id");
        question.setText(message);
        double latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        double longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

        if (message != null) {
            initializeMarker(latitude, longitude);
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map2)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void initializeMarker(double latitude, double longitude) {
        marker = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Location")
                .draggable(true);

        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(currentLatitude, currentLongitude)).zoom(10).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void doBuyItem(String task_id){
        BuyItemTask buyItemTask = new BuyItemTask() {
            @Override
            public void receiveData(String message) {
                if ( message != null )
                    Log.d("Final :" , message);
                else
                    Log.d("Final :" , "null" );
            }
        };
        buyItemTask.execute(BUY_ITEM_URL + "?task_id=" + task_id);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.yes) {
            doBuyItem(task);
        }
        else if (view.getId() == R.id.no) {

        }

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
