package com.example.umangkedia.helloworld;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by umang.kedia on 08/08/14.
 */


public interface GetItemsCallbackReceiver {
    public void receiveData(ArrayList<HashMap<String,String>> task_list);
}
