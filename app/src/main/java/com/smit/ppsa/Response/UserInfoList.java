package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoList {

    @SerializedName("n_st_id")
    @Expose
    private String nStId;
    @SerializedName("n_dis_id")
    @Expose
    private String nDisId;
    @SerializedName("n_tu_id")
    @Expose
    private String nTuId;
    @SerializedName("n_pm_id")
    @Expose
    private String nPmId;
    @SerializedName("n_pc_id")
    @Expose
    private String nPcId;
    @SerializedName("n_sfta_id")
    @Expose
    private String nSftaId;
    @SerializedName("oth_staff_id")
    @Expose
    private String othStaffId;

    public String getnStId() {
        return nStId;
    }

    public void setnStId(String nStId) {
        this.nStId = nStId;
    }

    public String getnDisId() {
        return nDisId;
    }

    public void setnDisId(String nDisId) {
        this.nDisId = nDisId;
    }

    public String getnTuId() {
        return nTuId;
    }

    public void setnTuId(String nTuId) {
        this.nTuId = nTuId;
    }

    public String getnPmId() {
        return nPmId;
    }

    public void setnPmId(String nPmId) {
        this.nPmId = nPmId;
    }

    public String getnPcId() {
        return nPcId;
    }

    public void setnPcId(String nPcId) {
        this.nPcId = nPcId;
    }

    public String getnSftaId() {
        return nSftaId;
    }

    public void setnSftaId(String nSftaId) {
        this.nSftaId = nSftaId;
    }

    public String getOthStaffId() {
        return othStaffId;
    }

    public void setOthStaffId(String othStaffId) {
        this.othStaffId = othStaffId;
    }
}
