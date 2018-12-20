package com.example.raghavgupta.chadisapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class qdisplay  extends AppCompatActivity {
    static StringBuilder response;
    TextView tv;
    TextView tvcont;
    public static StringBuilder text;

    public static StringBuilder main() throws Exception {
        URL url = new URL("https://dev.chadis.com/cschultz-chadis/respondent/api/patient/questionnaire/get-questions.do?id=" + tryQ.ret.toString());
        URLConnection c = url.openConnection();
        if(!(c instanceof HttpURLConnection))
            throw new IllegalStateException("Not HTTP?");

        // Convert the payload from String -> bytes (to get the length)
        // Use UTF-8 as the character encoding
        HttpURLConnection conn = (HttpURLConnection)c;

        // Configure network info
        conn.setConnectTimeout(5000); // 5s connection timeout
        conn.setReadTimeout(10000); // 10s read timeout
        conn.setInstanceFollowRedirects(false); // No reason to follow any HTTP redirection
        conn.setDoOutput(true); // We expect to write data to the server
        conn.setDoInput(true);  // We expect to read input from the server
        // Set HTTP headers
        conn.setRequestProperty("Accept", "application/json"); // only want JSON responses
        conn.setRequestProperty("User-Agent", "CHADIS Android Client v0.1"); // Always nice to advertise yourself
        conn.setRequestMethod("GET"); // We want a GET
        conn.setRequestProperty("Cookie","JSESSIONID=" + fetchQuest.sessionID);
        conn.connect(); // Actually connect

        OutputStream out = conn.getOutputStream();
        out.flush();
        out.close();


        int statusCode = conn.getResponseCode();
        if(200 == statusCode) {
            String contentType = conn.getHeaderField("Content-Type");

            int contentLimit = 10 * 1024; // 10k limit on what we are willing to accept
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            response = new StringBuilder(4096); // Default buffer size

            char[] buffer = new char[4096];
            int read;
            while(-1 < (read = in.read(buffer)))
                response.append(buffer, 0, read);
        } else {
            // TODO: maybe log complete server response somewhere
            // NOTE: must use conn.getErrorStream instead of conn.getInputStream
            throw new Exception("Cannot get questionnaires"); // TODO: Use app-specific exception type
        }
        return response;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qdisp);
        tv = findViewById(R.id.tvdisp);
        tvcont = findViewById(R.id.tvcont);
        try {
            text = qdisplay.main();
            JSONObject obj = new JSONObject(String.valueOf(text));
            JSONObject ar = new JSONObject(String.valueOf(obj.get("questionnaire")));
            String intro = ar.getString("introduction").replaceAll("(\\<[^>]*>)","");
            if(intro.isEmpty()){
                intro = ar.getString("description").replaceAll("(\\<[^>]*>)","");
            }
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setText(intro);

        } catch (Exception e) {
            e.printStackTrace();
        }

        tvcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(
                        qdisplay.this, questionnaires.class);
                startActivity(intent);
            }
        });
    }
}
