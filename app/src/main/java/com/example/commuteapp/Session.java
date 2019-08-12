package com.example.commuteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusername(String usename) {
        prefs.edit().putString("username", usename).commit();
    }

    public String getusename() {
        String usename = prefs.getString("username","");
        return usename;
    }


    public void setuserEmail(String userEmail) {
        prefs.edit().putString("userEmail", userEmail).commit();
    }

    public String getuserEmail() {
        String userEmail = prefs.getString("userEmail","");
        return userEmail;
    }

    public void setuserAddress(String userAddress) {
        prefs.edit().putString("userAddress", userAddress).commit();
    }

    public String getuserAddress() {
        String userAddress = prefs.getString("userAddress","");
        return userAddress;
    }


    public void setuserPhone(String userPhone) {
        prefs.edit().putString("userPhone", userPhone).commit();
    }

    public String getuserPhone() {
        String userPhone = prefs.getString("userPhone","");
        return userPhone;
    }
}
