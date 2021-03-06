package com.example.umangkedia.helloworld;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import javax.crypto.spec.GCMParameterSpec;

public class MyService extends Service {

    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10 ;
    private static String TAG = "MyService";

    public boolean isRunning = false;
    NotificationManager notificationManager;
    Location curLocation;
    public static boolean isService = true;

    private final String CHECK_POSITION_URL = "http://172.17.89.113:25500/shopping_item/check";
    private final String BUY_ITEM_URL = "http://172.17.89.113:25500//shopping_item/close";
    private final String GET_ITEMS_URL = "http://172.17.89.113:25500//shopping_item/fetch";
    private final String CREATE_GEO_FENCING_URL = "http://172.17.89.113:25500//shopping_item/create";

    private LocationManager locMan;
    private Boolean locationChanged;
    private Handler handler = new Handler();

    double latitude = 0;
    double longitude = 0;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //showNotification("Startup","dummy message");
        curLocation = getBestLocation();

        if (curLocation == null)
            Toast.makeText(getBaseContext(), "Unable to get your location", Toast.LENGTH_SHORT).show();
        else{
            //doCheckPositionRequest(String.valueOf(curLocation.getLatitude()), String.valueOf(curLocation.getLongitude()));
            //Toast.makeText(getBaseContext(), curLocation.toString(), Toast.LENGTH_LONG).show();
        }

        isService =  true;
    }


    @Override
    public synchronized void onDestroy() {

    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(GpsFinder, 1);

        Log.d(TAG, "onStart");
        if(!isRunning){
            isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void doCheckPositionRequest(String latitude, String longitude){
        CheckPositionTask checkPositionTask = new CheckPositionTask() {
            @Override
            public void receiveData(String mobile_id, String task_id, String description, String latitude, String longitude, String done, String distance) {
                Log.d("CheckPosition Request :" , mobile_id + ";" + task_id + ";" + description + ";" + latitude + ";" + longitude + ";" + done + ";" + distance);
                showNotification(task_id,description,latitude,longitude);
            }
        };
        checkPositionTask.execute(CHECK_POSITION_URL + "?latitude=" + latitude + "&longitude=" + longitude);
    }


    private Location getBestLocation() {
        Location gpslocation = null;
        Location networkLocation = null;

        if(locMan==null){
            locMan = (LocationManager) getApplicationContext() .getSystemService(getApplicationContext().LOCATION_SERVICE);
        }
        try {
            if(locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 1, gpsListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
                gpslocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
            if(locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 1, gpsListener);
                networkLocation = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (IllegalArgumentException e) {
            //Log.e(ErrorCode.ILLEGALARGUMENTERROR, e.toString());
            Log.e("error", e.toString());
        }
        if(gpslocation==null && networkLocation==null)
            return null;

        if(gpslocation!=null && networkLocation!=null){
            if(gpslocation.getTime() < networkLocation.getTime()){
                gpslocation = null;
                return networkLocation;
            }else{
                networkLocation = null;
                return gpslocation;
            }
        }
        if (gpslocation == null) {
            return networkLocation;
        }
        if (networkLocation == null) {
            return gpslocation;
        }
        return null;
    }

    public Runnable GpsFinder = new Runnable(){
        public void run(){

            Location tempLoc = getBestLocation();
//            Log.d(TAG, String.valueOf(tempLoc.getLatitude() + String.valueOf(tempLoc.getLongitude())));

            if(tempLoc!=null)
                curLocation = tempLoc;
            if (curLocation != null)
                doCheckPositionRequest(String.valueOf(curLocation.getLatitude()), String.valueOf(curLocation.getLongitude()));

            handler.postDelayed(GpsFinder,60000);// register again to start after 40 seconds...
        }
    };

    public void readWebPage(){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://google.com");
        // Get the response
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response_str = null;
        try {
            response_str = client.execute(request, responseHandler);
            if(!response_str.equalsIgnoreCase("")){
                Log.d(TAG, "Got Response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Show a notification while this service is running.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(String task_id, String description, String latitude, String longitude) {

        if (description != null && description.trim().length() > 0) {
            String msg = "Can you " + description + "?";

            Intent notificationIntent = new Intent(getApplicationContext(), BuyActivity.class);
            notificationIntent.putExtra("question", msg);
            notificationIntent.putExtra("task_id", task_id);
            notificationIntent.putExtra("latitude", latitude);
            notificationIntent.putExtra("longitude", longitude);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

            Notification n = new Notification.Builder(this)
                    .setContentTitle("Task to complete")
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.notify(0, n);
        }
    }

    LocationListener gpsListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (curLocation == null) {
                curLocation = location;
                locationChanged = true;
            }else if (curLocation.getLatitude() == location.getLatitude() && curLocation.getLongitude() == location.getLongitude()){
                locationChanged = false;
                return;
            }else
                locationChanged = true;

            curLocation = location;

            if (locationChanged)
                locMan.removeUpdates(gpsListener);

        }
        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status,Bundle extras) {
            if (status == 0)// UnAvailable
            {
            } else if (status == 1)// Trying to Connect
            {
            } else if (status == 2) {// Available
            }
        }

    };
}
