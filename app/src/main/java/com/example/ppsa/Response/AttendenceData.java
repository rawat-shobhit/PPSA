package com.example.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendenceData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("n_attend_typ")
    @Expose
    private String nAttendTyp;
    @SerializedName("d_rpt")
    @Expose
    private String dRpt;
    @SerializedName("n_user_id")
    @Expose
    private String nUserId;
    @SerializedName("d_cdat")
    @Expose
    private String dCdat;
    @SerializedName("d_mdat")
    @Expose
    private String dMdat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getnAttendTyp() {
        return nAttendTyp;
    }

    public void setnAttendTyp(String nAttendTyp) {
        this.nAttendTyp = nAttendTyp;
    }

    public String getdRpt() {
        return dRpt;
    }

    public void setdRpt(String dRpt) {
        this.dRpt = dRpt;
    }

    public String getnUserId() {
        return nUserId;
    }

    public void setnUserId(String nUserId) {
        this.nUserId = nUserId;
    }

    public String getdCdat() {
        return dCdat;
    }

    public void setdCdat(String dCdat) {
        this.dCdat = dCdat;
    }

    public String getdMdat() {
        return dMdat;
    }

    public void setdMdat(String dMdat) {
        this.dMdat = dMdat;
    }

}
