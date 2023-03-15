package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CounselingResponse {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user_data")
    @Expose
    private List<CounsellingResponseItem> user_data;


    public CounselingResponse(boolean status, String message, List<CounsellingResponseItem> user_data) {
        this.status = status;
        this.message = message;
        this.user_data = user_data;
    }

    public boolean isStatus() {
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

    public List<CounsellingResponseItem> getUser_data() {
        return user_data;
    }

    public void setUser_data(List<CounsellingResponseItem> user_data) {
        this.user_data = user_data;
    }
}
