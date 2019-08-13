package com.example.commuteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
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


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

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

    private final String KEY = "1Hbfh667adfDEJ78";
    private String ALGORITHM = "AES";


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
                String password = edPwd.getText().toString();
                final String verifyPass = edPwd2.getText().toString();
                final String emailid = edEmail.getText().toString().toLowerCase();
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


                //encrypt the password
                final String encryptedPass = encryptPassword(password);


                session = new Session(getApplicationContext());
                //check if we already have this user registered
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        //System.out.println("comeshgdhfdgjhkgh");
                                        userExists = true;

                                    }

                                } catch (ClassCastException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        if (userExists ) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                builder.setMessage("We have an account with this email ID, please login to the app")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                                return;

                        }
                        else {
                            ProfileValue profileValue = new ProfileValue();
                            profileValue.setuserName(name);
                            profileValue.setPassword(encryptedPass);
                            profileValue.setuserAddress(homeaddress);
                            profileValue.setuserEmail(emailid);
                            profileValue.setuserPhone(phone);

                            final Map<String, Object> dataMap = new HashMap<String, Object>();
                            String[] email = emailid.split("@");
                            String changeEmail = email[0].replace('.', '_');
                            // all validations are done so it is safe to put the data into the database
                            dataMap.put(changeEmail, profileValue.toMap());
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

    private String encryptPassword(String password) {

        Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
        String encryptedValue64 = " ";
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] encryptedByteValue = cipher.doFinal(password.getBytes("utf-8"));
            encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return encryptedValue64;
    }
}
