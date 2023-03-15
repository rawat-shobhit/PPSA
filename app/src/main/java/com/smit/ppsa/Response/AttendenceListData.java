package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendenceListData {

    @SerializedName("d_rpt")
    @Expose
    private String dRpt;
    @SerializedName("c_attend_typ")
    @Expose
    private String cAttendTyp;
    @SerializedName("Leave")
    @Expose
    private String leave;
    @SerializedName("n_user_id")
    @Expose
    private String nUserId;

    public String getdRpt() {
        return dRpt;
    }

    public void setdRpt(String dRpt) {
        this.dRpt = dRpt;
    }

    public String getcAttendTyp() {
        return cAttendTyp;
    }

    public void setcAttendTyp(String cAttendTyp) {
        this.cAttendTyp = cAttendTyp;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getnUserId() {
        return nUserId;
    }

    public void setnUserId(String nUserId) {
        this.nUserId = nUserId;
    }

}
