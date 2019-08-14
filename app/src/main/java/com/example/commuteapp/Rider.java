package com.example.commuteapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class Rider extends AppCompatActivity {
    private com.example.commuteapp.Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        session = new com.example.commuteapp.Session(getApplicationContext());
        TextView textView = (TextView) findViewById(R.id.info_tv_2);
        String t = session.getuserPerson()+"    "+session.getpersonPhone();
        textView.setText(t);

        System.out.println("Ggggggggggggggggg"+t);

    }

}
