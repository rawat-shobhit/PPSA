package com.example.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormOneData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("c_typ")
    @Expose
    private String cTyp;

    @SerializedName("c_st_nam")
    @Expose
    private String cStNam;
    @SerializedName("c_st_short")
    @Expose
    private String cStShort;

    @SerializedName("c_dis_nam")
    @Expose
    private String cDisNam;
    @SerializedName("c_dis_short")
    @Expose
    private String cDisShort;
    @SerializedName("n_st_id")
    @Expose
    private String nStId;
    @SerializedName("n_dis_id")
    @Expose
    private String nDisId;

    @SerializedName("c_tu_name")
    @Expose
    private String cTuName;

    @SerializedName("n_tu_id")
    @Expose
    String n_tu_id;

    @SerializedName("c_test_reas")
    @Expose
    String c_test_reas;

    @SerializedName("c_typ_specm")
    @Expose
    String c_typ_specm;

    @SerializedName("c_smpl_ext")
    @Expose
    String c_smpl_ext;

    @SerializedName("c_sputm_typ")
    @Expose
    String c_sputm_typ;

    @SerializedName("c_val")
    @Expose
    String c_val;



    public String getC_smpl_ext() {
        return c_smpl_ext;
    }

    public String getC_sputm_typ() {
        return c_sputm_typ;
    }

    public String getC_typ_specm() {
        return c_typ_specm;
    }

    public String getC_test_reas() {
        return c_test_reas;
    }

    public String getN_tu_id() {
        return n_tu_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcTyp() {
        return cTyp;
    }

    public void setcTyp(String cTyp) {
        this.cTyp = cTyp;
    }
    public String getcStNam() {
        return cStNam;
    }

    public void setcStNam(String cStNam) {
        this.cStNam = cStNam;
    }

    public String getcStShort() {
        return cStShort;
    }

    public void setcStShort(String cStShort) {
        this.cStShort = cStShort;
    }

    public String getcDisNam() {
        return cDisNam;
    }

    public void setcDisNam(String cDisNam) {
        this.cDisNam = cDisNam;
    }

    public String getcDisShort() {
        return cDisShort;
    }

    public void setcDisShort(String cDisShort) {
        this.cDisShort = cDisShort;
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

    public String getcTuName() {
        return cTuName;
    }

    public void setcTuName(String cTuName) {
        this.cTuName = cTuName;
    }




}
