package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrevVisitsData {
    @SerializedName("d_visit")
    @Expose
    private String dVisit;
    @SerializedName("c_val")
    @Expose
    private String cVal;
    @SerializedName("n_hf_id")
    @Expose
    private String nHfId;
    @SerializedName("n_tu_id")
    @Expose
    private String n_tu_id;
    @SerializedName("n_qty")
    @Expose
    private String n_qty;
    @SerializedName("d_issue")
    @Expose
    private String d_issue="";
    @SerializedName("tot_drug")
    @Expose
    private String tot_drug;
    @SerializedName("n_st_id")
    @Expose
    private String n_st_id;
    @SerializedName("n_dis_id")
    @Expose
    private String n_dis_id;
    @SerializedName("n_doc_id")
    @Expose
    private String n_doc_id;
    @SerializedName("d_disp")
    @Expose
    private String d_disp="";

    public String getD_disp() {
        return d_disp;
    }

    public void setD_disp(String d_disp) {
        this.d_disp = d_disp;
    }

    public String getD_issue() {
        return d_issue;
    }

    public void setD_issue(String d_issue) {
        this.d_issue = d_issue;
    }

    public String getTot_drug() {
        return tot_drug;
    }

    public void setTot_drug(String tot_drug) {
        this.tot_drug = tot_drug;
    }

    public String getN_st_id() {
        return n_st_id;
    }

    public void setN_st_id(String n_st_id) {
        this.n_st_id = n_st_id;
    }

    public String getN_dis_id() {
        return n_dis_id;
    }

    public void setN_dis_id(String n_dis_id) {
        this.n_dis_id = n_dis_id;
    }

    public String getN_doc_id() {
        return n_doc_id;
    }

    public void setN_doc_id(String n_doc_id) {
        this.n_doc_id = n_doc_id;
    }

    public String getN_tu_id() {
        return n_tu_id;
    }

    public void setN_tu_id(String n_tu_id) {
        this.n_tu_id = n_tu_id;
    }

    public String getN_qty() {
        return n_qty;
    }

    public void setN_qty(String n_qty) {
        this.n_qty = n_qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private String id;

    public String getdVisit() {
        return dVisit;
    }

    public void setdVisit(String dVisit) {
        this.dVisit = dVisit;
    }

    public String getcVal() {
        return cVal;
    }

    public void setcVal(String cVal) {
        this.cVal = cVal;
    }

    public String getnHfId() {
        return nHfId;
    }

    public void setnHfId(String nHfId) {
        this.nHfId = nHfId;
    }
}
