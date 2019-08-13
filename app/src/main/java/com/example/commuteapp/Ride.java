package com.example.commuteapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Ride extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new com.example.commuteapp.Session(getApplicationContext());

        TextView textView = (TextView) findViewById(R.id.info_tv_2);
        textView.setText("We will notify you once we have a match");

        FloatingActionButton fab = findViewById(R.id.profile_fab_2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ride.this, Profile.class);
                intent.putExtra("name", session.getusername());
                intent.putExtra("emailid", session.getuserEmail());
                intent.putExtra("phone", session.getuserPhone());
                intent.putExtra("homeaddress", session.getuserAddress());

                Ride.this.startActivity(intent);
            }
        });


    }

}
