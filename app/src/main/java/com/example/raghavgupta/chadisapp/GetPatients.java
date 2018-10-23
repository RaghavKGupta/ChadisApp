package com.example.raghavgupta.chadisapp;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class GetPatients  {
    static StringBuilder response;
    public static String sessionID;
    public static StringBuilder main(String[] args) throws Exception {
        URL url = new URL("https://dev.chadis.com/cschultz-chadis/respondent/api/patients.do");
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
        conn.setRequestProperty("Cookie","JSESSIONID=" + args[0]);
        conn.setRequestMethod("GET"); // We want a POST
        sessionID = args[0];
        conn.connect(); // Actually connect

        OutputStream out = conn.getOutputStream();
        //out.write(bytes);
        out.flush();
        out.close();


        int statusCode = conn.getResponseCode();
        if(200 == statusCode) {
            String contentType = conn.getHeaderField("Content-Type");
            if(!("application/json".equals(contentType) || "application/json;charset=UTF-8".equals(contentType)))
                throw new IllegalStateException("Unexpected server response content-type: " + contentType);

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
            throw new Exception("Cannot get patients"); // TODO: Use app-specific exception type
        }
        return response;
    }

    public static StringBuilder getResponse() {
        return response;
    }
}






