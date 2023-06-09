package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HospitalList implements Serializable {
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
    @SerializedName("c_tu_name")
    @Expose
    private String cTuName;
    @SerializedName("n_hf_cd")
    @Expose
    private String nHfCd;
    @SerializedName("c_hf_nam")
    @Expose
    private String cHfNam;
    @SerializedName("c_hf_typ")
    @Expose
    private String cHfTyp;
    @SerializedName("c_hf_addr")
    @Expose
    private String cHfAddr;
    @SerializedName("c_cont_per")
    @Expose
    private String cContPer;
    @SerializedName("c_cp_mob")
    @Expose
    private String cCpMob;
    @SerializedName("c_cp_email")
    @Expose
    private String cCpEmail;
    @SerializedName("pm_code")
    @Expose
    private String pmCode;
    @SerializedName("pm_short")
    @Expose
    private String pmShort;
    @SerializedName("pm_lng")
    @Expose
    private String pmLng;
    @SerializedName("pm_nam")
    @Expose
    private String pmNam;
    @SerializedName("pc_code")
    @Expose
    private String pcCode;
    @SerializedName("pc_short")
    @Expose
    private String pcShort;
    @SerializedName("pc_lng")
    @Expose
    private String pcLng;
    @SerializedName("pc_nam")
    @Expose
    private String pcNam;
    @SerializedName("stf_code")
    @Expose
    private String stfCode;
    @SerializedName("stf_short")
    @Expose
    private String stfShort;
    @SerializedName("stf_lng")
    @Expose
    private String stfLng;
    @SerializedName("stf_nam")
    @Expose
    private String stfNam;
    @SerializedName("all_code")
    @Expose
    private String allCode;
    @SerializedName("all_short")
    @Expose
    private String allShort;
    @SerializedName("oth_lng")
    @Expose
    private String othLng;
    @SerializedName("oth_nam")
    @Expose
    private String othNam;
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
    @SerializedName("n_pm_sanc_id")
    @Expose
    private String nPmSancId;
    @SerializedName("n_pc_sanc_id")
    @Expose
    private String nPcSancId;
    @SerializedName("n_stf_sanc_id")
    @Expose
    private String nStfSancId;
    @SerializedName("pm_staff_id")
    @Expose
    private String pmStaffId;
    @SerializedName("pc_staff_id")
    @Expose
    private String pcStaffId;
    @SerializedName("stf_staff_id")
    @Expose
    private String stfStaffId;
    @SerializedName("oth_staff_id")
    @Expose
    private String othStaffId;

    public String getN_pp_idenr() {
        return n_pp_idenr;
    }

    public void setN_pp_idenr(String n_pp_idenr) {
        this.n_pp_idenr = n_pp_idenr;
    }

    @SerializedName("n_pp_idenr")
    @Expose
    private String n_pp_idenr;
    @SerializedName("n_hf_typ_id")
    @Expose
    private String nHfTypId;

    @SerializedName("lst_visit")
    @Expose
    private String lst_visit;

    @SerializedName("no_of_days")
    @Expose
    private String no_of_days;

    public String getLst_visit() {
        return lst_visit;
    }

    public void setLst_visit(String lst_visit) {
        this.lst_visit = lst_visit;
    }

    public String getNo_of_days() {
        return no_of_days;
    }

    public void setNo_of_days(String no_of_days) {
        this.no_of_days = no_of_days;
    }

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public String getcTuName() {
        return cTuName;
    }

    public void setcTuName(String cTuName) {
        this.cTuName = cTuName;
    }

    public String getnHfCd() {
        return nHfCd;
    }

    public void setnHfCd(String nHfCd) {
        this.nHfCd = nHfCd;
    }

    public String getcHfNam() {
        return cHfNam;
    }

    public void setcHfNam(String cHfNam) {
        this.cHfNam = cHfNam;
    }

    public String getcHfTyp() {
        return cHfTyp;
    }

    public void setcHfTyp(String cHfTyp) {
        this.cHfTyp = cHfTyp;
    }

    public String getcHfAddr() {
        return cHfAddr;
    }

    public void setcHfAddr(String cHfAddr) {
        this.cHfAddr = cHfAddr;
    }

    public String getcContPer() {
        return cContPer;
    }

    public void setcContPer(String cContPer) {
        this.cContPer = cContPer;
    }

    public String getcCpMob() {
        return cCpMob;
    }

    public void setcCpMob(String cCpMob) {
        this.cCpMob = cCpMob;
    }

    public String getcCpEmail() {
        return cCpEmail;
    }

    public void setcCpEmail(String cCpEmail) {
        this.cCpEmail = cCpEmail;
    }

    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
    }

    public String getPmShort() {
        return pmShort;
    }

    public void setPmShort(String pmShort) {
        this.pmShort = pmShort;
    }

    public String getPmLng() {
        return pmLng;
    }

    public void setPmLng(String pmLng) {
        this.pmLng = pmLng;
    }

    public String getPmNam() {
        return pmNam;
    }

    public void setPmNam(String pmNam) {
        this.pmNam = pmNam;
    }

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getPcShort() {
        return pcShort;
    }

    public void setPcShort(String pcShort) {
        this.pcShort = pcShort;
    }

    public String getPcLng() {
        return pcLng;
    }

    public void setPcLng(String pcLng) {
        this.pcLng = pcLng;
    }

    public String getPcNam() {
        return pcNam;
    }

    public void setPcNam(String pcNam) {
        this.pcNam = pcNam;
    }

    public String getStfCode() {
        return stfCode;
    }

    public void setStfCode(String stfCode) {
        this.stfCode = stfCode;
    }

    public String getStfShort() {
        return stfShort;
    }

    public void setStfShort(String stfShort) {
        this.stfShort = stfShort;
    }

    public String getStfLng() {
        return stfLng;
    }

    public void setStfLng(String stfLng) {
        this.stfLng = stfLng;
    }

    public String getStfNam() {
        return stfNam;
    }

    public void setStfNam(String stfNam) {
        this.stfNam = stfNam;
    }

    public String getAllCode() {
        return allCode;
    }

    public void setAllCode(String allCode) {
        this.allCode = allCode;
    }

    public String getAllShort() {
        return allShort;
    }

    public void setAllShort(String allShort) {
        this.allShort = allShort;
    }

    public String getOthLng() {
        return othLng;
    }

    public void setOthLng(String othLng) {
        this.othLng = othLng;
    }

    public String getOthNam() {
        return othNam;
    }

    public void setOthNam(String othNam) {
        this.othNam = othNam;
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

    public String getnPmSancId() {
        return nPmSancId;
    }

    public void setnPmSancId(String nPmSancId) {
        this.nPmSancId = nPmSancId;
    }

    public String getnPcSancId() {
        return nPcSancId;
    }

    public void setnPcSancId(String nPcSancId) {
        this.nPcSancId = nPcSancId;
    }

    public String getnStfSancId() {
        return nStfSancId;
    }

    public void setnStfSancId(String nStfSancId) {
        this.nStfSancId = nStfSancId;
    }

    public String getPmStaffId() {
        return pmStaffId;
    }

    public void setPmStaffId(String pmStaffId) {
        this.pmStaffId = pmStaffId;
    }

    public String getPcStaffId() {
        return pcStaffId;
    }

    public void setPcStaffId(String pcStaffId) {
        this.pcStaffId = pcStaffId;
    }

    public String getStfStaffId() {
        return stfStaffId;
    }

    public void setStfStaffId(String stfStaffId) {
        this.stfStaffId = stfStaffId;
    }

    public String getOthStaffId() {
        return othStaffId;
    }

    public void setOthStaffId(String othStaffId) {
        this.othStaffId = othStaffId;
    }

    public String getnHfTypId() {
        return nHfTypId;
    }

    public void setnHfTypId(String nHfTypId) {
        this.nHfTypId = nHfTypId;
    }
}
