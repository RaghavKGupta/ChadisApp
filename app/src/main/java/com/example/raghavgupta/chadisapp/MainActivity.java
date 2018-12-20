package com.example.raghavgupta.chadisapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import java.net.URI;
import java.util.Locale;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.content.res.Configuration;
import android.content.Context;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static EditText userID;
    public static EditText psd;
    private fetchData fd;
    private Button loginButton;
    public static TextView incorrectPrompt;
    private TextView register;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean saveLogin;
    CheckBox saveLoginCheckBox;
    Switch langChange;
    WebView webview;
    Button webBut;
    Boolean flag = true;
    public static Boolean flag2 = false;
    public static String val;
    public static String temp;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userID = findViewById(R.id.etID);
        psd = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btnLogin);
        incorrectPrompt = findViewById(R.id.tvIncorrect);
        register = findViewById(R.id.tvNMember);
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
        saveLoginCheckBox = findViewById(R.id.checkBox);
        editor = sharedPreferences.edit();
        langChange = findViewById(R.id.langSwitch);
        webBut = findViewById(R.id.webButton);


        android.support.v7.widget.Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);


        // fix this
        langChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    userID.setHint("Ingresar ID");
                    psd.setHint("introducir la contraseña");
                    loginButton.setText("iniciar sesión");
                    saveLoginCheckBox.setText("Recuérdame");
                    register.setText("No es un miembro? Haga clic para registrarse");
                    flag = false;
                } else{
                    userID.setHint("Enter ID");
                    psd.setHint("Enter password");
                    loginButton.setText("login");
                    saveLoginCheckBox.setText("Remember me");
                    register.setText("Not a member? Click to register");
                    flag = true;
                }


            }
        });
        final Context context = this;

        webBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Chadis on the web");

                WebView wv = new WebView(context);
                wv.loadUrl("https://dev.chadis.com/cschultz-chadis/auth/login.do;jsessionid=");
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {
                fd = (fetchData) new fetchData(new AsyncResponse() {
                    @Override
                    public void processFinishLogin(StringBuilder result) {

                        if(result!=null){
                            temp = String.valueOf(result.toString().substring(result.toString().indexOf("session")));
                            val = temp.substring(temp.indexOf(":\"")+2,temp.indexOf("\"}}"));

                            try {
                                validLogin();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            incorrectPrompt.setText("incorrect");
                        }
                    }
                });

                try {
                    fd.execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

       saveLogin = sharedPreferences.getBoolean("saveLogin",true);
       if(saveLogin == true){
           userID.setText(sharedPreferences.getString("userID",null));
           psd.setText(sharedPreferences.getString("psd",null));
       }

    }

    public static String getVal() {
        return val;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    public void validLogin() throws Exception {
            android.content.Intent intent = new android.content.Intent(
                MainActivity.this, SecondActivity.class);
        startActivity(intent);

            if(saveLoginCheckBox.isChecked()){
                editor.putBoolean("saveLogin",true);
                editor.putString("userID",userID.getText().toString());
                editor.putString("psd",psd.getText().toString());
                editor.commit();
            }

    }

}
