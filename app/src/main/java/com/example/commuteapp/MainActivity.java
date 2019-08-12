package com.example.commuteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button login_btn, signup_btn;
    private EditText edEmail, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup_btn = (Button)findViewById(R.id.btn_signup);
        login_btn = (Button) findViewById(R.id.btn_login);
        edEmail = (EditText)findViewById(R.id.ed_username);
        edPassword = (EditText) findViewById(R.id.ed_password);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailId = edEmail.getText().toString();
                final String password = edPassword.getText().toString();

                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
}
