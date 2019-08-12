package com.example.commuteapp;

import android.app.Application;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProfileValue extends Application implements Serializable {
    private String userName = " ";
    private String userAddress = " ";
    private String userEmail = " ";
    private String userPhone = " ";

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }


    public String getuserAddress() {
        return userAddress;
    }

    public void setuserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getuserEmail() {
        return userEmail;
    }

    public void setuserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getuserPhone() {
        return userPhone;
    }

    public void setuserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


//    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", userName);
        result.put("userAddress", userAddress);
        result.put("userEmail", userEmail);
        result.put("phone", userPhone);

        return result;
    }
}