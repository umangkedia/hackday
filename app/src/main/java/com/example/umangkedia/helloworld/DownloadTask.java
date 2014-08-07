package com.example.umangkedia.helloworld;

/**
 * Created by umang.kedia on 08/08/14.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A class, to download Places from Geocoding webservice */
public abstract class DownloadTask extends AsyncTask<String, Void, String> implements  CallbackReciever{
    private Activity activity;

    ObjectMapper mapper = new ObjectMapper();
    private ProgressDialog mProgressDialog;

    String data = null;

    public static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyBrzWx3eYxgjWD8qywQW3TO4QHpnvpfvMg&";

    public DownloadTask(Activity activity) {
        this.activity = activity;
    }

    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(String... url) {
        try{
            data = downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("MapActivity: Background Task", e.toString());
        }
        return data;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        Log.d("MapActivity", result);
        try {

            Map<String, Object> results = mapper.readValue(result, Map.class);
            String status = (String) results.get("status");

            if (status.equalsIgnoreCase("OK")) {
                List<Object> resultList = (List<Object>) results.get("results");
                Map<String, Object> resultMap = (Map<String, Object>) resultList.get(0);
                Map<String, Object> geometry = (HashMap<String, Object>) resultMap.get("geometry");
                Map<String, Object> location = (Map<String, Object>) geometry.get("location");
                double latitude = (Double) location.get("lat");
                double longitude = (Double) location.get("lng");

                if (latitude != 0 && longitude != 0) {
                    receiveData(latitude, longitude);
                }
            }

            else
                Toast.makeText(activity, "Location not found",
                        Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String downloadUrl(String encodedURL) throws IOException {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(GEOCODE_URL + encodedURL);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }
}