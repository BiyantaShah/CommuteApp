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
    private String userType = " ";
    private String userCount = " ";
    private String password =" ";

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getUserType() {return userType;}

    public void setUserType(String userType) {this.userType = userType;}

    public String getUserCount() {return userCount;}

    public  void setUserCount(String userCount) {this.userCount = userCount;}


//    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", userName);
        result.put("password", password);
        result.put("userAddress", userAddress);
        result.put("userEmail", userEmail);
        result.put("phone", userPhone);
        result.put("type", userType);
        result.put("count", userCount);

        return result;
    }
}