package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QualificationResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_data")
    @Expose
    private ArrayList<QualificationList> userData = null;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<QualificationList> getUserData() {
        return userData;
    }

    public void setUserData(ArrayList<QualificationList> userData) {
        this.userData = userData;
    }

}
