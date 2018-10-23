package com.example.raghavgupta.chadisapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private static EditText userID;
    private static EditText psd;
    private static EditText email;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userID = findViewById(R.id.etID);
        psd = findViewById(R.id.etPassword);
        email = findViewById(R.id.etEmail);
        registerButton = findViewById(R.id.btnRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validRegister()){
                    // do database here
                }
            }
        });
    }

    private Boolean validRegister(){
        Boolean result = false;
        String uID = userID.getText().toString();
        String upsd = psd.getText().toString();
        String uemail = email.getText().toString();

        if(uID.isEmpty() || upsd.isEmpty() || uemail.isEmpty()){
            Toast.makeText(this, "Please enter all details.", Toast.LENGTH_SHORT).show();
        } else{
            result = true;
        }
        return result;
    }
}
