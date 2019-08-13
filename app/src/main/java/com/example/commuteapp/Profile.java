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

public class Profile extends AppCompatActivity {

    private EditText edName;
    private EditText edEmail;
    private EditText edpwd;
    private EditText edpwd2;
    private EditText edAddress;
    private EditText edPhone;
    private Button update_btn;

    private final String KEY = "1Hbfh667adfDEJ78";
    private String ALGORITHM = "AES";

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


                // updating only changed fields
                String changedUid = session.getuserEmail().split("@")[0].replace('.','_');
                myRef.child(changedUid).child("userAddress").setValue(addressChanged);
                myRef.child(changedUid).child("phone").setValue(phoneChanged);

                if (!passwordChanged.isEmpty()) {

                    // encrypt password and then pass it
                    String encryptedPass = encryptPassword(passwordChanged);
                    myRef.child(changedUid).child("password").setValue(encryptedPass);

                }





                Intent intent = new Intent(Profile.this, Home.class);
                startActivity(intent);
            }
        });


    }

    private String encryptPassword(String passwordChanged) {
        Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
        String encryptedValue64 = " ";
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] encryptedByteValue = cipher.doFinal(passwordChanged.getBytes("utf-8"));
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
