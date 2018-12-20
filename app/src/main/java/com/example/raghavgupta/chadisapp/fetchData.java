package com.example.raghavgupta.chadisapp;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import static android.support.v4.content.ContextCompat.startActivity;


@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class fetchData extends AsyncTask <String[], Void,Void>
{
    public AsyncResponse delegate = null;
    public StringBuilder response;
    public int count = 0;

    public fetchData(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public StringBuilder doInBackground(String[] args) throws Exception{
            URL url = new URL("https://dev.chadis.com/cschultz-chadis/respondent/api/login.do");
            URLConnection c = url.openConnection();
            if(!(c instanceof HttpURLConnection))
                throw new IllegalStateException("Not HTTP?");

            String username = args[0];

            String password = args[1];

            // HTTP FORM data: username=uid&password=pwd
            String payload = URLEncoder.encode("username", "UTF-8")
                    + '='
                    + URLEncoder.encode(username, "UTF-8")
                    + '&'
                    + URLEncoder.encode("password", "UTF-8")
                    + '='
                    + URLEncoder.encode(password, "UTF-8")
                    ;

            byte[] bytes = payload.getBytes(Charset.forName("UTF-8"));

            int contentLength = bytes.length;

            // Convert the payload from String -> bytes (to get the length)
            // Use UTF-8 as the character encoding
            HttpURLConnection conn = (HttpURLConnection)c;

            // Configure network info
            conn.setConnectTimeout(5000); // 5s connection timeout
            conn.setReadTimeout(10000); // 10s read timeout
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Cookie","JSESSIONID=");// No reason to follow any HTTP redirection
            conn.setDoOutput(true); // We expect to write data to the server
            conn.setDoInput(true);  // We expect to read input from the server

        // call get request on session object
        // key - Cookie\
        // value - JSESSIONID= the actual session id from the parent auth

            // Set HTTP headers
            conn.setRequestProperty("Accept", "application/json"); // only want JSON responses
            conn.setRequestProperty("User-Agent", "CHADIS Android Client v0.1"); // Always nice to advertise yourself
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST"); // We want a POST
            conn.setFixedLengthStreamingMode(contentLength); // Sets Content-Length header, does not use "chunked" encoding

            conn.connect(); // Actually connect

            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();


            int statusCode = conn.getResponseCode();
            if(200 == statusCode) {
                String contentType = conn.getHeaderField("Content-Type");
                if(!("application/json".equals(contentType) || "application/json;charset=UTF-8".equals(contentType)))
                    throw new IllegalStateException("Unexpected server response content-type: " + contentType);

                int contentLimit = 10 * 1024; // 10k limit on what we are willing to accept
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                contentLength = conn.getHeaderFieldInt("Content-Length", -1);

                if(-1 < contentLength) {
                    if(contentLength > contentLimit)
                        throw new IllegalArgumentException("Content-length is unacceptably long: " + contentLength);
                    // Known content-length
                    response = new StringBuilder(contentLength);
                } else {
                    response = new StringBuilder(4096); // Default buffer size
                }

                char[] buffer = new char[4096];
                int read;
                while(-1 < (read = in.read(buffer)))
                    response.append(buffer, 0, read);
            } else {
                // TODO: maybe log complete server response somewhere
                // NOTE: must use conn.getErrorStream instead of conn.getInputStream
                conn.getErrorStream();
            }

            count++;
        while (count==1) {
            // work...
            if (isCancelled()) break;
            return response;
        }
            return response;
        }

    @Override
    protected void onCancelled(Void aVoid) {
        delegate.processFinishLogin(getResult());
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected Void doInBackground(String[]... strings) {
        final String[] myArray= new String[2];
        myArray[0] = MainActivity.userID.getText().toString();
        myArray[1] = MainActivity.psd.getText().toString();

        try {
            doInBackground(myArray);
            super.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public StringBuilder getResult(){
        return response;
    }
}