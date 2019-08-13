package com.example.commuteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.se.omapi.Session;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    private com.example.commuteapp.Session session; //global variable
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Users");;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new com.example.commuteapp.Session(getApplicationContext());
        //System.out.println("djfhsjkhgfjfgh" + session.getuserEmail());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Profile.class);
                intent.putExtra("name", session.getusername());
                intent.putExtra("emailid", session.getuserEmail());
                intent.putExtra("phone", session.getuserPhone());
                intent.putExtra("homeaddress", session.getuserAddress());
                Home.this.startActivity(intent);
            }
        });

        Button drive_button = findViewById(R.id.btn_drive);
        drive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String[] no_riders = {"2", "3"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Pick the number of riders");
                        builder.setItems(no_riders, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.setuserCount(no_riders[which]);
                                session.setuserType("d");
                                String changedUid = session.getuserEmail().split("@")[0].replace('.','_');
                                myRef.child(changedUid).child("type").setValue(session.getuserType());
                                myRef.child(changedUid).child("count").setValue(session.getuserCount());

                                Intent intent = new Intent(Home.this, Drive.class);
                                startActivity(intent);

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        Button ride_button = findViewById(R.id.btn_ride);
        ride_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setuserCount("0");
                session.setuserType("r");
                String changedUid = session.getuserEmail().split("@")[0].replace('.','_');
                myRef.child(changedUid).child("type").setValue(session.getuserType());
                myRef.child(changedUid).child("count").setValue(session.getuserCount());
                Intent intent = new Intent(Home.this, Ride.class);
                startActivity(intent);
            }
        });

    }



}
