package com.example.commuteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private EditText edName;
    private EditText edEmail;
    private EditText edpwd;
    private EditText edpwd2;
    private EditText edAddress;
    private EditText edPhone;
    private Button update_btn;

    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edName = (EditText) findViewById(R.id.ed_name);
        edName.setEnabled(false); //constant
        edEmail = (EditText) findViewById(R.id.ed_email);
        edEmail.setEnabled(false); //constant
        edPhone = (EditText) findViewById(R.id.ed_phone);
        edAddress = (EditText) findViewById(R.id.ed_address);
        edpwd = (EditText) findViewById(R.id.ed_pwd);
        edpwd2 = (EditText) findViewById(R.id.ed_pwd_2);

        update_btn = (Button) findViewById(R.id.btn_update);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String emailID = intent.getStringExtra("emailid");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("homeaddress");

        edName.setText(name); // unchangeable
        edEmail.setText(emailID); // unchangeable
        edPhone.setText(phone);
        edAddress.setText(address);



        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneChanged = edPhone.getText().toString();
                String addressChanged = edAddress.getText().toString();
                String passwordChanged = edpwd.getText().toString();
                String verifyPwd = edpwd2.getText().toString();

                System.out.println("jdfhsjkhf phone changed "+ phoneChanged +" address changed "+ addressChanged );

                if (!passwordChanged.equals(verifyPwd)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                    builder.setMessage("Both passwords need to match")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();

                    return;
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users");

                //updating the session variable
                session = new Session(getApplicationContext());
                session.setusername(name);
                session.setuserEmail(emailID);
                session.setuserAddress(addressChanged);
                session.setuserPhone(phoneChanged);

                String userType = session.getuserType();
                String passCount = session.getuserCount();

                //setting the values to profile
                ProfileValue profileValue = new ProfileValue();
                profileValue.setuserAddress(addressChanged);
                profileValue.setuserPhone(phoneChanged);
                profileValue.setuserName(name);
                profileValue.setuserEmail(emailID);
                profileValue.setUserType(userType);
                profileValue.setUserCount(passCount);

                final Map<String, Object> dataMap = new HashMap<String, Object>();

                String key = emailID.split("@")[0]; // this is the record to update

                dataMap.put(key, profileValue.toMap());
                myRef.updateChildren(dataMap);



                Intent intent = new Intent(Profile.this, Home.class);
                startActivity(intent);
            }
        });


    }
}
