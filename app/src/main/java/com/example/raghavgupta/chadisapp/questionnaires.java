package com.example.raghavgupta.chadisapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class questionnaires extends AppCompatActivity {
    TextView tv;
    TextView contbtn;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest);

        tv = findViewById(R.id.textView5);
        contbtn = findViewById(R.id.contQ);

        StringBuilder quest = qdisplay.text;
        try {
            JSONObject obj = new JSONObject(String.valueOf(quest));
            JSONArray ar = (JSONArray) obj.get("questions");
            String question = ar.get(0).toString().replaceAll("(\\<[^>]*>)", "");
            JSONObject json = new JSONObject(question);
            tv.setText((CharSequence) json.get("text"));

            JSONArray array = (JSONArray) obj.get("questionTypes");
            String q = array.get(0).toString().replaceAll("(\\<[^>]*>)", "");
            JSONObject j = new JSONObject(q);
            String multiplicity = j.get("multiplicity").toString();
            String displaytype = j.get("displaytype").toString();
            String options = j.get("options").toString();
            String[] text = new String[20];
            JSONArray j1 = new JSONArray(options);
            for (int i = 0; i < j1.length(); i++) {
                String s = j1.get(i).toString();
                JSONObject r = new JSONObject(s);
                text[i] = r.get("text").toString();
            }
            if (displaytype.equals("choice")) {
                RadioGroup rg = new RadioGroup(this);
                rg.setOrientation(LinearLayout.VERTICAL);
                for (int i = 0; i < j1.length(); i++) {
                    RadioButton rdbtn = new RadioButton(this);
                    rdbtn.setText(text[i]);
                    rg.addView(rdbtn);
                }
                ((ViewGroup) findViewById(R.id.radiogroup)).addView(rg);
            }

            } catch(JSONException e){
                e.printStackTrace();
            }


            contbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // add button here with function
                }
            });

    }
}
