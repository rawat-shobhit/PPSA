package com.example.ppsa.Response;

import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorsList {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @SerializedName("c_doc_nam")
    @Expose
    private String cDocNam;
    @SerializedName("c_qualf")
    @Expose
    private String cQualf;
    @SerializedName("c_qual")
    @Expose
    private String cQual;
    @SerializedName("c_mob")
    @Expose
    private String cMob;
    @SerializedName("n_hf_id")
    @Expose
    private String nHfId;
    @SerializedName("id")
    @Expose
    private String id;

    public DoctorsList(String cDocNam, String cQualf, String cQual, String cMob, String nHfId) {
        this.cDocNam = cDocNam;
        this.cQualf = cQualf;
        this.cQual = cQual;
        this.cMob = cMob;
        this.nHfId = nHfId;
    }

    public DoctorsList() {
    }

    public String getcDocNam() {
        return cDocNam;
    }

    public void setcDocNam(String cDocNam) {
        this.cDocNam = cDocNam;
    }

    public String getcQualf() {
        return cQualf;
    }

    public void setcQualf(String cQualf) {
        this.cQualf = cQualf;
    }

    public String getcQual() {
        return cQual;
    }

    public void setcQual(String cQual) {
        this.cQual = cQual;
    }

    public String getcMob() {
        return cMob;
    }

    public void setcMob(String cMob) {
        this.cMob = cMob;
    }

    public String getnHfId() {
        return nHfId;
    }

    public void setnHfId(String nHfId) {
        this.nHfId = nHfId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
