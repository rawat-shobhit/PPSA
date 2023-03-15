package com.example.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormTwoSpinnerData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("c_val")
    @Expose
    private String cVal;

    @SerializedName("error")
    @Expose
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcVal() {
        return cVal;
    }

    public void setcVal(String cVal) {
        this.cVal = cVal;
    }

}
