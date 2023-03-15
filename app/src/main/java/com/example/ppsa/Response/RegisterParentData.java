package com.example.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterParentData {


    @SerializedName("d_reg_dat")
    @Expose
    private String dRegDat;
    @SerializedName("n_nksh_id")
    @Expose
    private String nNkshId;
    @SerializedName("c_pat_nam")
    @Expose
    private String cPatNam;
    @SerializedName("n_age")
    @Expose
    private String nAge;
    @SerializedName("c_typ")
    @Expose
    private String cTyp;
    @SerializedName("n_wght")
    @Expose
    private String nWght;
    @SerializedName("n_hght")
    @Expose
    private String nHght;
    @SerializedName("c_add")
    @Expose
    private String cAdd;
    @SerializedName("c_taluka")
    @Expose
    private String cTaluka;
    @SerializedName("c_town")
    @Expose
    private String cTown;
    @SerializedName("c_ward")
    @Expose
    private String cWard;
    @SerializedName("c_lnd_mrk")
    @Expose
    private String cLndMrk;
    @SerializedName("n_pin")
    @Expose
    private String nPin;
    @SerializedName("c_doc_nam")
    @Expose
    private String cDocNam;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("n_st_id")
    @Expose
    private String nStId;
    @SerializedName("n_dis_id")
    @Expose
    private String nDisId;
    @SerializedName("n_tu_id")
    @Expose
    private String nTuId;
    @SerializedName("n_hf_id")
    @Expose
    private String nHfId;
    @SerializedName("n_doc_id")
    @Expose
    private String nDocId;
    @SerializedName("n_user_id")
    @Expose
    private String nUserId;

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getdRegDat() {
        return dRegDat;
    }

    public void setdRegDat(String dRegDat) {
        this.dRegDat = dRegDat;
    }

    public String getnNkshId() {
        return nNkshId;
    }

    public void setnNkshId(String nNkshId) {
        this.nNkshId = nNkshId;
    }

    public String getcPatNam() {
        return cPatNam;
    }

    public void setcPatNam(String cPatNam) {
        this.cPatNam = cPatNam;
    }

    public String getnAge() {
        return nAge;
    }

    public void setnAge(String nAge) {
        this.nAge = nAge;
    }

    public String getcTyp() {
        return cTyp;
    }

    public void setcTyp(String cTyp) {
        this.cTyp = cTyp;
    }

    public String getnWght() {
        return nWght;
    }

    public void setnWght(String nWght) {
        this.nWght = nWght;
    }

    public String getnHght() {
        return nHght;
    }

    public void setnHght(String nHght) {
        this.nHght = nHght;
    }

    public String getcAdd() {
        return cAdd;
    }

    public void setcAdd(String cAdd) {
        this.cAdd = cAdd;
    }

    public String getcTaluka() {
        return cTaluka;
    }

    public void setcTaluka(String cTaluka) {
        this.cTaluka = cTaluka;
    }

    public String getcTown() {
        return cTown;
    }

    public void setcTown(String cTown) {
        this.cTown = cTown;
    }

    public String getcWard() {
        return cWard;
    }

    public void setcWard(String cWard) {
        this.cWard = cWard;
    }

    public String getcLndMrk() {
        return cLndMrk;
    }

    public void setcLndMrk(String cLndMrk) {
        this.cLndMrk = cLndMrk;
    }

    public String getnPin() {
        return nPin;
    }

    public void setnPin(String nPin) {
        this.nPin = nPin;
    }

    public String getcDocNam() {
        return cDocNam;
    }

    public void setcDocNam(String cDocNam) {
        this.cDocNam = cDocNam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getnHfId() {
        return nHfId;
    }

    public void setnHfId(String nHfId) {
        this.nHfId = nHfId;
    }

    public String getnDocId() {
        return nDocId;
    }

    public void setnDocId(String nDocId) {
        this.nDocId = nDocId;
    }

    public String getnUserId() {
        return nUserId;
    }

    public void setnUserId(String nUserId) {
        this.nUserId = nUserId;
    }
}
