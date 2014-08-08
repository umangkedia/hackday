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
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service{

    private static String TAG = "MyService";
    private MyThread mythread;
    public boolean isRunning = false;
    NotificationManager notificationManager;

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
        Log.d(TAG, "onStart");
        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

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

}
