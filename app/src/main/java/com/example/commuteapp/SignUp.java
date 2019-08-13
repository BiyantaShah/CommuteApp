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

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";
    private Button signup_btn;
    private EditText edName;
    private EditText edEmail;
    private EditText edPwd;
    private EditText edPwd2;
    private EditText edAddress;
    private EditText edphone;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_btn = (Button)findViewById(R.id.btn_signup);
        edName = (EditText)findViewById(R.id.ed_name);
        edEmail = (EditText)findViewById(R.id.ed_email);
        edPwd = (EditText)findViewById(R.id.ed_pwd);
        edPwd2 = (EditText)findViewById(R.id.ed_pwd_2);
        edAddress = (EditText)findViewById(R.id.ed_address);
        edphone = (EditText)findViewById(R.id.ed_phone);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = edName.getText().toString();
                final String password = edPwd.getText().toString();
                final String verifyPass = edPwd2.getText().toString();
                final String emailid = edEmail.getText().toString();
                final String homeaddress = edAddress.getText().toString();
                final String phone = edphone.getText().toString();

                // check for none of the fields to be empty
                if (name.isEmpty() || password.isEmpty() || verifyPass.isEmpty() ||emailid.isEmpty() || homeaddress.isEmpty() || phone.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setMessage("Please enter values in all the fields")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                    return;
                }


                // password and verify password should be same
                if (!password.equals(verifyPass)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setMessage("Both passwords should match")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                    return;
                }

                //check if email id is present in correct format.
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("Users");
                String[] email = emailid.split("@");
                if (!emailid.contains("@")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setMessage("Please enter a valid Citrix given email id")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                    return;
                } // correct email id and now if its valid
                else if (!email[1].equals("citrix.com")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setMessage("Please enter your citrix email id")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();

                    return;

                }


                session = new Session(getApplicationContext());
                //check if we already have this user registered
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean userExists = false;
                        int count = 0;
                        if (dataSnapshot.exists()) {
                            GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                            };
                            Map<String, Object> dataMap = dataSnapshot.getValue(genericTypeIndicator);


                            for (String key : dataMap.keySet()) {
                                Object data = dataMap.get(key);

                                System.out.println("jekhfsjdhf key " +key );
                                try {
                                    HashMap<String, Object> userData = (HashMap<String, Object>) data;
                                    String emails = (String) userData.get("userEmail");

                                    System.out.println("emails from db" + emails + "email given" + emailid );
                                    // check if user exists
                                    if (emails.equals(emailid)) {
                                        System.out.println("comeshgdhfdgjhkgh");
                                        userExists = true;

                                    }

                                } catch (ClassCastException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        if (userExists ) {
                            String sessionEmail = session.getuserEmail();
                            if(!sessionEmail.equals(emailid)) {
                               // if session user is not same as entered user then its an existing user.
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                builder.setMessage("We have an account with this email ID, please login to the app")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                                return;
                            }

                        }
                        else {
                            ProfileValue profileValue = new ProfileValue();
                            profileValue.setuserName(name);
                            profileValue.setuserAddress(homeaddress);
                            profileValue.setuserEmail(emailid);
                            profileValue.setuserPhone(phone);

                            final Map<String, Object> dataMap = new HashMap<String, Object>();
                            String[] email = emailid.split("@");

                            // all validations are done so it is safe to put the data into the database
                            dataMap.put(email[0], profileValue.toMap());
                            myRef.updateChildren(dataMap);
                            session.setusername(name);
                            session.setuserEmail(emailid);
                            session.setuserAddress(homeaddress);
                            session.setuserPhone(phone);

                            Intent intent = new Intent(SignUp.this, Home.class);
                            startActivity(intent);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
