package com.example.commuteapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Drive extends AppCompatActivity {
    private com.example.commuteapp.Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new com.example.commuteapp.Session(getApplicationContext());

        TextView textView = (TextView) findViewById(R.id.info_tv);
        textView.setText("\t"+"You will be picking up "+session.getuserCount()+" riders");

        FloatingActionButton fab = findViewById(R.id.profile_fab_1);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Drive.this, Profile.class);
                intent.putExtra("name", session.getusername());
                intent.putExtra("emailid", session.getuserEmail());
                intent.putExtra("phone", session.getuserPhone());
                intent.putExtra("homeaddress", session.getuserAddress());

                Drive.this.startActivity(intent);
            }
        });


        Button ride_button = findViewById(R.id.btn_change);
        ride_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Drive.this, Home.class);
                startActivity(intent);
            }
        });

    }


}
