package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendenceTypeData {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("c_attend_typ")
    @Expose
    private String cAttendTyp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcAttendTyp() {
        return cAttendTyp;
    }

    public void setcAttendTyp(String cAttendTyp) {
        this.cAttendTyp = cAttendTyp;
    }
}
