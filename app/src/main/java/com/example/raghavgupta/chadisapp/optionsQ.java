package com.example.raghavgupta.chadisapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class optionsQ extends AppCompatActivity {
    Button b1;
    Button b2;
    Button b3;
    Button b4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);

        final Context context = this;

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(
                        optionsQ.this, qdisplay.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(
                        optionsQ.this, web.class);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(
                        optionsQ.this, tryQ.class);
                startActivity(intent);
            }
        });
    }


}
