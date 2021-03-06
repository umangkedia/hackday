package com.example.umangkedia.helloworld;

/**
 * Created by umang.kedia on 08/08/14.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A class, to download Places from Geocoding webservice */
public abstract class GetItemsTask extends AsyncTask<String, Void, String> implements  GetItemsCallbackReceiver {

    ObjectMapper mapper = new ObjectMapper();
    private ProgressDialog mProgressDialog;
    String data = null;

    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(String... url) {
        try{
            data = get_http_response(url[0]);
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
            ArrayList<Object> read_arrayList = mapper.readValue(result, ArrayList.class);
            ArrayList<HashMap<String,String>> task_list = new ArrayList<HashMap<String,String>>();
            while (read_arrayList.iterator().hasNext())
            {
                HashMap<String,Object> read_hashmap =  (HashMap<String,Object>) read_arrayList.iterator().next();
                HashMap<String,String> hash_map = new HashMap<String, String>();
                hash_map.put("mobile_id", ((String) read_hashmap.get("mobile_id")));
                hash_map.put("task_id", ((String) read_hashmap.get("task_id")));
                hash_map.put("description", ((String) read_hashmap.get("description")));
                hash_map.put("latitude", (String.valueOf (read_hashmap.get("latitude"))));
                hash_map.put("longitude", (String.valueOf (read_hashmap.get("longitude"))));
                hash_map.put("done", String.valueOf(read_hashmap.get("longitude")));
                task_list.add(hash_map);
            }
            receiveData(task_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String get_http_response(String input_url) throws IOException {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(input_url);

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