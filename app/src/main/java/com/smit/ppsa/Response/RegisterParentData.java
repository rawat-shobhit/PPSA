package com.smit.ppsa.Response;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterParentData implements Serializable {


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
    @SerializedName("diag_test")
    @Expose
    private String diag_test;
    @SerializedName("trans_out")
    @Expose
    private String trans_out;
    @SerializedName("c_hf_typ")
    @Expose
    private String c_hf_typ;

    public String getC_hf_typ() {
        return c_hf_typ;
    }

    public String getTrans_out() {
        return trans_out;
    }

    public String getDiag_test() {
        return diag_test;
    }

    public void setDiag_test(String diag_test) {
        this.diag_test = diag_test;
    }

    public String getC_hf_nam() {
        return c_hf_nam;
    }

    public void setC_hf_nam(String c_hf_nam) {
        this.c_hf_nam = c_hf_nam;
    }

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
    @SerializedName("test_reas")
    @Expose
    private String testReas;
    @SerializedName("specm_typ")
    @Expose
    private String specmTyp;
    @SerializedName("d_specm_col")
    @Expose
    private String dSpecmCol;
    @SerializedName("c_plc_samp_col")
    @Expose
    private String cPlcSampCol;
    @SerializedName("smpl_ext")
    @Expose
    private String smplExt;
    @SerializedName("sputm_sampl")
    @Expose
    private String sputmSampl;

    @SerializedName("no_days")
    @Expose
    private String no_days;
    @SerializedName("LST_DT")
    @Expose
    private String LST_DT;
    @SerializedName("c_mob")
    @Expose
    private String c_mob;

    @SerializedName("c_mob_2")
    @Expose
    private String c_mob_2;

    @SerializedName("c_hf_nam")
    @Expose
    private String c_hf_nam;

    @SerializedName("n_diag_cd")
    @Expose
    private int n_diag_cd;
    @SerializedName("n_sex")
    @Expose
    private String n_sex = "";
    @SerializedName("smpl_col_id")
    @Expose
    private String smpl_col_id;
    @SerializedName("n_enroll_id")
    @Expose
    private String n_enroll_id;
    @SerializedName("n_smpl_col_id")
    @Expose
    private String n_smpl_col_id;
    @SerializedName("n_rpt_col_id")
    @Expose
    private String n_rpt_col_id;
    @SerializedName("c_aadh_img")
    @Expose
    private String c_aadh_img;
    @SerializedName("c_presc_img")
    @Expose
    private String c_presc_img;
    @SerializedName("c_bnk_img")
    @Expose
    private String c_bnk_img;
    @SerializedName("c_tst_rpt_img")
    @Expose
    private String c_tst_rpt_img;
    @SerializedName("d_lst_dispn")
    @Expose
    private String d_lst_dispn;

    public String getD_lst_dispn() {
        return d_lst_dispn;
    }

    public String getC_aadh_img() {
        return c_aadh_img;
    }

    public String getC_presc_img() {
        return c_presc_img;
    }

    public String getC_bnk_img() {
        return c_bnk_img;
    }

    public String getC_tst_rpt_img() {
        return c_tst_rpt_img;
    }

    public String getN_rpt_col_id() {
        return n_rpt_col_id;
    }

    public String getN_smpl_col_id() {
        return n_smpl_col_id;
    }


    public String getN_enroll_id() {
        return n_enroll_id;
    }

    public void setN_enroll_id(String n_enroll_id) {
        this.n_enroll_id = n_enroll_id;
    }

    public String getSmpl_col_id() {
        return smpl_col_id;
    }

    public void setSmpl_col_id(String smpl_col_id) {
        this.smpl_col_id = smpl_col_id;
    }

    public String getN_sex() {
        return n_sex;
    }

    public void setN_sex(String n_sex) {
        this.n_sex = n_sex;
    }

    public String getC_mob() {
        return c_mob;
    }

    public void setC_mob(String c_mob) {
        this.c_mob = c_mob;
    }

    public String getC_mob_2() {
        return c_mob_2;
    }

    public void setC_mob_2(String c_mob_2) {
        this.c_mob_2 = c_mob_2;
    }

    public int getN_diag_cd() {
        return n_diag_cd;
    }

    public void setN_diag_cd(int n_diag_cd) {
        this.n_diag_cd = n_diag_cd;
    }

    public void setTestReas(String testReas) {
        this.testReas = testReas;
    }

    public void setSpecmTyp(String specmTyp) {
        this.specmTyp = specmTyp;
    }

    public void setdSpecmCol(String dSpecmCol) {
        this.dSpecmCol = dSpecmCol;
    }

    public void setcPlcSampCol(String cPlcSampCol) {
        this.cPlcSampCol = cPlcSampCol;
    }

    public void setSmplExt(String smplExt) {
        this.smplExt = smplExt;
    }

    public void setSputmSampl(String sputmSampl) {
        this.sputmSampl = sputmSampl;
    }

    public String getNo_days() {
        return no_days;
    }

    public void setNo_days(String no_days) {
        this.no_days = no_days;
    }

    public String getLST_DT() {
        return LST_DT;
    }

    public void setLST_DT(String LST_DT) {
        this.LST_DT = LST_DT;
    }

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

    public String getcPatMob() {
        return c_mob;
    }

    public void setcPatMob(String cPatNam) {
        this.c_mob = c_mob;
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

    public String getTestReas() {
        return testReas;
    }

    public String getSpecmTyp() {
        return specmTyp;
    }

    public String getdSpecmCol() {
        return dSpecmCol;
    }

    public String getcPlcSampCol() {
        return cPlcSampCol;
    }

    public String getSmplExt() {
        return smplExt;
    }

    public String getSputmSampl() {
        return sputmSampl;
    }
}
