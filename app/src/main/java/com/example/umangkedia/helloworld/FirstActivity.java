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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Logger;


public class FirstActivity extends Activity implements View.OnClickListener {

    private  Button mapButton;
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

        String message = getIntent().getStringExtra("MESSAGE");

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
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
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
