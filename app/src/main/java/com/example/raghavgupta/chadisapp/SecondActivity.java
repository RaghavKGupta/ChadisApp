package com.example.raghavgupta.chadisapp;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import java.util.Collections;
import java.util.TreeMap;

public class SecondActivity extends AppCompatActivity {


    public static ListView lv;
    private static String text;
    public static TreeMap<String, Object> nameMap = new TreeMap<String, Object>();
    public static TreeMap<Object, Object> dobMap = new TreeMap<Object, Object>();

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        lv = findViewById(R.id.listView);


        try {
            GetPatients.main(new String[]{MainActivity.getVal()});
            text = GetPatients.getResponse().toString();

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            JSONObject object = new JSONObject(text);
            JSONArray nest = (JSONArray) object.get("patients");
            String[] listItems = new String[nest.length()];
            for(int i=0;i<nest.length();i++){
                String s = nest.getJSONObject(i).get("first").toString() + " " +
                        nest.getJSONObject(i).get("middle").toString() + " " +
                        nest.getJSONObject(i).get("last").toString();
                nameMap.put(s,nest.getJSONObject(i).get("id"));
                listItems[i] = s;
                dobMap.put(nest.getJSONObject(i).get("id"),nest.getJSONObject(i).get("dob"));
            }


            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1,listItems);

            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String value=adapter.getItem(position);
                    String [] p = new String[2];
                    p[0] = GetPatients.sessionID;
                    // don't find based on the name
                    p[1] = nameMap.get(value).toString();
                    try {
                        fetchQuest.main(p);
                        android.content.Intent intent = new android.content.Intent(
                                SecondActivity.this, tryQ.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
