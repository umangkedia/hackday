package com.example.umangkedia.helloworld;

/**
 * Created by pavan.g on 08/08/14.
 */

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import android.provider.Settings.Secure;

public class TaskListView extends ListActivity {
    private final String GET_ITEMS_URL = "http://172.17.89.113:25500/shopping_item/fetch";
    private final String TASK_STRING = "description";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String STATUS = "done";
    private SimpleAdapter sa;
    //private String android_id = Secure.getString(getApplicationContext().getContentResolver(),
    //        Secure.ANDROID_ID);
    private String android_id = "mobile_id";
    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.listrowlayout);
        System.out.println("Android id is:" + android_id);
        doGetItems(android_id);
        sa = new SimpleAdapter(this, list,
                R.layout.mytwolines,
                new String[] { "line1","line2" },
                new int[] {R.id.line_a, R.id.line_b});

        setListAdapter( sa );
    }


    private void doGetItems(String mobile_id) {
        GetItemsTask getItemsTask = new GetItemsTask() {
            @Override
            public void receiveData(ArrayList<HashMap<String, String>> task_list) {
                list.clear();
                for (HashMap<String, String> item: task_list) {
                    HashMap<String, String> rowItem = new HashMap<String, String>();
                    rowItem.put("line1", item.get(TASK_STRING));
                    rowItem.put("line2", "lat: " + item.get(LATITUDE) + " long: " + item.get(LONGITUDE) + " Status: " + item.get(STATUS));
                    list.add(rowItem);
                }
                //finish();
                //startActivity(getIntent());
            }
        };
        Log.d("URL is", GET_ITEMS_URL+ "?mobile_id=" + mobile_id);
        getItemsTask.execute(GET_ITEMS_URL+ "?mobile_id=" + mobile_id);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }
}
