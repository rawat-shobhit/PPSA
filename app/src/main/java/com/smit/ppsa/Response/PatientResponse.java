package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_data")
    @Expose
    private List<RegisterParentData> userData = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RegisterParentData> getUserData() {
        return userData;
    }

    public void setUserData(List<RegisterParentData> userData) {
        this.userData = userData;
    }

}
