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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.Map;


public class FirstActivity extends Activity implements View.OnClickListener {

    private  Button mapButton;

    private RequestQueue mRequestQueue = null;
    private final String BASE_URL = "http://mobileapi.flipkart.net/2/discover/getSearch?store=search.flipkart.com&start=0&count=10&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);
        startService(new Intent(FirstActivity.this, MyService.class));
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

    @Override
    public void onClick(View view) {

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
