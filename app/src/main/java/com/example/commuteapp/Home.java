package com.example.commuteapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.se.omapi.Session;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {
    private com.example.commuteapp.Session session; //global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new com.example.commuteapp.Session(getApplicationContext());
        System.out.println("djfhsjkhgfjfgh" + session.getuserEmail());

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
    }

}
