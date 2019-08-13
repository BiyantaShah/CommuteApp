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

public class MainActivity extends AppCompatActivity {
    private Button login_btn, signup_btn;
    private EditText edEmail, edPassword;
    private Session session;

    private final String KEY = "1Hbfh667adfDEJ78";
    private String ALGORITHM = "AES";

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
                final String emailId = edEmail.getText().toString().toLowerCase();
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

                // session instances should be cleared out before we proceed to add a new session instance
                session = new Session(getApplicationContext());
                session.setuserEmail(" ");
                session.setusername(" ");
                session.setuserPhone(" ");
                session.setuserAddress(" ");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    String passwd = (String)userData.get("password");
                                    String email = (String)userData.get("userEmail");

                                    //to set context
                                    String uname = (String)userData.get("username");
                                    String address = (String) userData.get("userAddress");
                                    String phoneNumber = (String) userData.get("phone");
                                    //ending set to context
                                    String uid = emailId.split("@")[0].replace('.', '_');
                                    System.out.println("The email id is "+key + " and their phone is "+passwd);

                                    //decrypt password
                                    String decrypt = decrypting(passwd);
                                    System.out.println("hgfdfgsdfg "+decrypt);

                                    // only checking for the user entered on the logging screen, not all users

                                    if(uid.equals(key)){

                                        userExists = true;
                                        if(email.equals(emailId) && decrypt.equals(password)) {

                                            session.setusername(uname);
                                            session.setuserEmail(email);
                                            session.setuserAddress(address);
                                            session.setuserPhone(phoneNumber);

                                            Intent intent = new Intent(MainActivity.this, Home.class);
                                            startActivity(intent);
                                        }
                                        else {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setMessage("Incorrect credentials, please enter the correct credentials")
                                                        .setNegativeButton("Retry", null)
                                                        .create()
                                                        .show();

                                                return;

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

    private String decrypting(String passwd) {

        Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
        Cipher cipher = null;
        String decryptedValue = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decryptedValue64 = Base64.decode(passwd, Base64.DEFAULT);
            byte [] decryptedByteValue = new byte[0];
            decryptedByteValue = cipher.doFinal(decryptedValue64);

            decryptedValue = new String(decryptedByteValue,"utf-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decryptedValue;
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
