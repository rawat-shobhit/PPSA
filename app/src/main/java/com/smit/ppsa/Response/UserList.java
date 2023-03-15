package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserList implements Serializable {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("c_name")
    @Expose
    private String cName;
    @SerializedName("c_username")
    @Expose
    private String cUsername;
    @SerializedName("c_password")
    @Expose
    private String cPassword;
    @SerializedName("c_email")
    @Expose
    private String cEmail;
    @SerializedName("c_mobile")
    @Expose
    private String cMobile;
    @SerializedName("n_status")
    @Expose
    private String nStatus;
    @SerializedName("n_st_cd")
    @Expose
    private String nStCd;
    @SerializedName("n_dis_cd")
    @Expose
    private String nDisCd;
    @SerializedName("n_blk_cd")
    @Expose
    private String nBlkCd;
    @SerializedName("n_vlg_cd")
    @Expose
    private String nVlgCd;
    @SerializedName("d_created")
    @Expose
    private String dCreated;
    @SerializedName("d_modified")
    @Expose
    private String dModified;
    @SerializedName("n_user_ses")
    @Expose
    private String nUserSes;
    @SerializedName("n_user_level")
    @Expose
    private String nUserLevel;
    @SerializedName("n_access_rights")
    @Expose
    private String nAccessRights;
    @SerializedName("c_lng_desc")
    @Expose
    private String c_lng_desc="";
    @SerializedName("c_st_nam")
    @Expose
    private String c_st_nam="";
    @SerializedName("n_staff_sanc")
    @Expose
    private String n_staff_sanc="";

    public String getN_staff_sanc() {
        return n_staff_sanc;
    }

    public String getC_st_nam() {
        return c_st_nam;
    }

    public String getC_lng_desc() {
        return c_lng_desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcUsername() {
        return cUsername;
    }

    public void setcUsername(String cUsername) {
        this.cUsername = cUsername;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcMobile() {
        return cMobile;
    }

    public void setcMobile(String cMobile) {
        this.cMobile = cMobile;
    }

    public String getnStatus() {
        return nStatus;
    }

    public void setnStatus(String nStatus) {
        this.nStatus = nStatus;
    }

    public String getnStCd() {
        return nStCd;
    }

    public void setnStCd(String nStCd) {
        this.nStCd = nStCd;
    }

    public String getnDisCd() {
        return nDisCd;
    }

    public void setnDisCd(String nDisCd) {
        this.nDisCd = nDisCd;
    }

    public String getnBlkCd() {
        return nBlkCd;
    }

    public void setnBlkCd(String nBlkCd) {
        this.nBlkCd = nBlkCd;
    }

    public String getnVlgCd() {
        return nVlgCd;
    }

    public void setnVlgCd(String nVlgCd) {
        this.nVlgCd = nVlgCd;
    }

    public String getdCreated() {
        return dCreated;
    }

    public void setdCreated(String dCreated) {
        this.dCreated = dCreated;
    }

    public String getdModified() {
        return dModified;
    }

    public void setdModified(String dModified) {
        this.dModified = dModified;
    }

    public String getnUserSes() {
        return nUserSes;
    }

    public void setnUserSes(String nUserSes) {
        this.nUserSes = nUserSes;
    }

    public String getnUserLevel() {
        return nUserLevel;
    }

    public void setnUserLevel(String nUserLevel) {
        this.nUserLevel = nUserLevel;
    }

    public String getnAccessRights() {
        return nAccessRights;
    }

    public void setnAccessRights(String nAccessRights) {
        this.nAccessRights = nAccessRights;
    }

}
