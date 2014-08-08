package com.example.umangkedia.helloworld;

/**
 * Created by umang.kedia on 08/08/14.
 */


public interface CheckPositionCallbackReceiver {
    public void receiveData(String mobile_id, String task_id,String description, String latitude, String longitude, String done, String distance);
}
