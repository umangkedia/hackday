package com.example.umangkedia.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


public class FirstActivity extends Activity implements View.OnClickListener {

    private  Button mapButton;

    private RequestQueue mRequestQueue = null;
    private final String BASE_URL = "http://mobileapi.flipkart.net/2/discover/getSearch?store=search.flipkart.com&start=0&count=10&q=";
    private final String CHECK_POSITION_URL = "http://172.17.89.113:25500/shopping_item/check";
    private final String BUY_ITEM_URL = "http://172.17.89.113:25500//shopping_item/close";
    private final String GET_ITEMS_URL = "http://172.17.89.113:25500//shopping_item/fetch";
    private final String CREATE_GEO_FENCING_URL = "http://172.17.89.113:25500//shopping_item/create";
    public static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);
        startService(new Intent(FirstActivity.this, MyService.class));
        onNewIntent(getIntent());

    }

    @Override
    public void onNewIntent(Intent newIntent) {
        this.setIntent(newIntent);
        Log.d(TAG, "Activity Launched through Notification");

        String message = getIntent().getStringExtra("question");

        if (message != null) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            markCompleted();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked, don't do anything
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

        // Now getIntent() returns the updated Intent
    }

    private void markCompleted() {
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

    private void doCheckPositionRequest(String latitude, String longitude){
        CheckPositionTask checkPositionTask = new CheckPositionTask() {
            @Override
            public void receiveData(String mobile_id, String task_id, String description, String latitude, String longitude, String done, String distance) {
               Log.d("Final :" , mobile_id + ";" + task_id + ";" + description + ";" + latitude + ";" + longitude + ";" + done + ";" + distance);
            }
        };
        checkPositionTask.execute(CHECK_POSITION_URL + "?latitude=" + latitude + "&longitude=" + longitude);
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

    private void doGetItems(String mobile_id) {
        GetItemsTask getItemsTask = new GetItemsTask() {
            @Override
            public void receiveData(ArrayList<HashMap<String, String>> task_list) {
                Log.d("GetItems","Nothing to show");
            }
        };
        getItemsTask.execute(GET_ITEMS_URL+ "?mobile_id=" + mobile_id);
    }

    private void doCreateGeoFencing(String mobile_id, String task_id, String description, String latitude, String longitude){
        CreateGeoFencingTask createGeoFencingTask = new CreateGeoFencingTask() {
            @Override
            public void receiveData(String message) {
                Log.d("CreateGeoFencing", message);
            }
        };
        createGeoFencingTask.execute(CREATE_GEO_FENCING_URL + "?mobile_id=" + mobile_id
        + "&task_id=" + task_id + "&latitude=" + latitude + "&longitude=" + longitude
                + "&description=" + description);
    }

        @Override
    public void onClick(View view) {
        //doCheckPositionRequest("1","2");
        //doBuyItem("task_id");
        //doGetItems("mobile_id");
        //doCreateGeoFencing("mobile_id","task_id12", "description", "12.21","21.12");

        if (view.getId() == R.id.mapButton) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivityForResult(intent, 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            //fetch value from intent
            double latitude = data.getDoubleExtra(MapActivity.LATITUDE, 0);
            double longitude = data.getDoubleExtra(MapActivity.LONGITUDE, 0);

            // Set the message string in textView
            Toast.makeText(FirstActivity.this, "Lat: " + latitude + " "  + "Long: " + longitude,
                    Toast.LENGTH_LONG).show();

        }
    }
}
