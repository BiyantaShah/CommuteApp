package com.example.commuteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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

                //check for whether a valid email id has been added
                if(!emailId.contains("@")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please enter a valid email id")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();

                    return;
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        boolean userExists = false;
                        if (dataSnapshot.exists())
                        {

                            GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                            Map<String, Object> dataMap = dataSnapshot.getValue(genericTypeIndicator );

                            for (String key: dataMap.keySet()) {
                                Object data = dataMap.get(key);

                                try{
                                    HashMap<String, Object> userData = (HashMap<String, Object>) data;
                                    String phoneNumber = (String)userData.get("phone");
                                    String email = (String)userData.get("userEmail");
                                    String[] uid = emailId.split("@");
                                    System.out.println("The email id is "+key + " and their phone is "+phoneNumber);

                                    // only checking for the user entered on the logging screen, not all users
                                    if(uid[0].equals(key)){
                                        userExists = true;
                                        if(email.equals(emailId) && phoneNumber.equals(password)) {
                                            Intent intent = new Intent(MainActivity.this, Home.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("Incorrect credentials, please enter the correct credentials")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();
                                        }
                                    }

                                }
                                catch (ClassCastException e) {
                                    e.printStackTrace();
                                }

                            }

                            if (!userExists) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("This account does not exist, please sign up")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("");
                    }
                });


            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
