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

    public String getusername() {
        String username = prefs.getString("username","");
        return username;
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

    public void setuserType(String userType) {
        prefs.edit().putString("userType", userType).commit();
    }
    public String getuserType() {
        String userPhone = prefs.getString("userType","");
        return userPhone;
    }

    public void setuserCount(String userCount) {
        prefs.edit().putString("userCount", userCount).commit();
    }
    public String getuserCount() {
        String userCount = prefs.getString("userCount","");
        return userCount;
    }

    public void setuserPerson(String person) {
        prefs.edit().putString("person", person).commit();
    }
    public String getuserPerson() {
        String person = prefs.getString("person","");
        return person;
    }

    public void setpersonPhone(String personPhone) {
        prefs.edit().putString("personPhone", personPhone).commit();
    }
    public String getpersonPhone() {
        String personPhone = prefs.getString("personPhone","");
        return personPhone;
    }

    public void setpersonAd(String personAd) {
        prefs.edit().putString("personAd", personAd).commit();
    }
    public String getpersonAd() {
        String personAd = prefs.getString("personAd","");
        return personAd;
    }
}
