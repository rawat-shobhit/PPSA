package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
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

    public String getLst_vst() {
        return lst_vst;
    }

    public void setLst_vst(String lst_vst) {
        this.lst_vst = lst_vst;
    }

    @SerializedName("lst_vst")
    @Expose
    private String lst_vst;

    public DoctorsList(String cDocNam, String cQualf, String cQual, String cMob, String nHfId, String lst_vst) {
        this.cDocNam = cDocNam;
        this.lst_vst = lst_vst;
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
