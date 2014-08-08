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
    private MyThread mythread;
    public boolean isRunning = false;
    NotificationManager notificationManager;
    Location curLocation;
    public static boolean isService = true;

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
        showNotification();
        mythread  = new MyThread();

        curLocation = getBestLocation();

        if (curLocation == null)
            Toast.makeText(getBaseContext(), "Unable to get your location", Toast.LENGTH_SHORT).show();
        else{
            //Toast.makeText(getBaseContext(), curLocation.toString(), Toast.LENGTH_LONG).show();
        }

        isService =  true;
    }


    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(GpsFinder, 1);

        Log.d(TAG, "onStart");
        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
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
            Log.d(TAG, String.valueOf(tempLoc.getLatitude() + String.valueOf(tempLoc.getLongitude())));

            if(tempLoc!=null)
                curLocation = tempLoc;

            handler.postDelayed(GpsFinder,40000);// register again to start after 40 seconds...
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

    class MyThread extends Thread{
        static final long DELAY = 30000;
        @Override
        public void run(){
            while(isRunning){
                Log.d(TAG,"Running");
                try {
                    readWebPage();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Show a notification while this service is running.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification() {
        String msg = "Click to see the notification";

        Intent notificationIntent = new Intent(getApplicationContext(), FirstActivity.class);
        notificationIntent.putExtra("MESSAGE", msg);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("Task to complete")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(0, n);
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
