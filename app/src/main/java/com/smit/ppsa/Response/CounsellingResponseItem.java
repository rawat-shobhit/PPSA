package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CounsellingResponseItem {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("c_val")
    @Expose
    private String c_val;


    public CounsellingResponseItem(String id, String c_val) {
        this.id = id;
        this.c_val = c_val;
    }


    public String getC_val() {
        return c_val;
    }

    public void setC_val(String c_val) {
        this.c_val = c_val;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
