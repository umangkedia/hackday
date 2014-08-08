package com.example.umangkedia.helloworld;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by umang.kedia on 08/08/14.
 */
public class locListener implements LocationListener {
    private double latService;
    private double lngService;

    @Override
    public void onLocationChanged(Location location) {
        latService = location.getLatitude();
        lngService = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}



}
