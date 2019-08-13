package com.example.commuteapp;

import android.content.Intent;
import android.net.Uri;
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
        textView.setText("\t" + "You will be picking up " + session.getuserCount() + " riders");

        FloatingActionButton fab = findViewById(R.id.profile_fab_1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Drive.this, Navigation.class);
                //startActivity(intent);

                //Uri gmmIntentUri = Uri.parse("google.navigation:q=3051+S+palm+aire,+3250+S+palm+aire");
                String url1 = "https://www.google.com/maps/dir/?api=1&destination=851 W Cypress Creek Rd, Fort Lauderdale&waypoints=";
                String url2 = "1100 Powerline Rd|1350 S Powerline rd";
                String url3 = "&travelmode=driving&dir_action=navigate";

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url1+url2+url3));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


    }


}
