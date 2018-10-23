package com.example.raghavgupta.chadisapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class web extends AppCompatActivity {

    public static WebView wv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weblay);


        wv = findViewById(R.id.webFull);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("view.do?id")) {
                    finish();
                    android.content.Intent intent = new android.content.Intent(
                            web.this, tryQ.class);
                    startActivity(intent);

                } else {
                    view.loadUrl(url);
                    return true;
                }
                return true;
            }

        });
        wv.loadUrl("https://dev.chadis.com/cschultz-chadis/respondent/questionnaire/continue.do;" +
                "jsessionid=" + GetPatients.sessionID + "?id=" + tryQ.ret.toString());

    }


    @Override
    public void onBackPressed()
    {
        if(wv.canGoBack()){
            wv.goBack();
        }else{
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit!")
                    .setMessage("Are you sure you want to close?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            android.content.Intent intent = new android.content.Intent(
                                    web.this, tryQ.class);
                            startActivity(intent);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

}
