package com.example.ppsa.Response;

import com.example.ppsa.HospitalsList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HospitalResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_data")
    @Expose
    private List<HospitalList> userData = null;

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

    public List<HospitalList> getUserData() {
        return userData;
    }

    public void setUserData(List<HospitalList> userData) {
        this.userData = userData;
    }

}
