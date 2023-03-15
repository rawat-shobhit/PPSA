package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SampleData {
    @SerializedName("test_reas")
    @Expose
    private String testReas;
    @SerializedName("diag_test")
    @Expose
    private String diag_test;
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
    @SerializedName("n_enroll_id")
    @Expose
    private String nEnrollId;
    @SerializedName("n_user_id")
    @Expose
    private String nUserId;
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
    @SerializedName("id")
    @Expose
    private String id;

    public String getDiag_test() {
        return diag_test;
    }

    public void setDiag_test(String diag_test) {
        this.diag_test = diag_test;
    }

    public String getTestReas() {
        return testReas;
    }

    public void setTestReas(String testReas) {
        this.testReas = testReas;
    }

    public String getSpecmTyp() {
        return specmTyp;
    }

    public void setSpecmTyp(String specmTyp) {
        this.specmTyp = specmTyp;
    }

    public String getdSpecmCol() {
        return dSpecmCol;
    }

    public void setdSpecmCol(String dSpecmCol) {
        this.dSpecmCol = dSpecmCol;
    }

    public String getcPlcSampCol() {
        return cPlcSampCol;
    }

    public void setcPlcSampCol(String cPlcSampCol) {
        this.cPlcSampCol = cPlcSampCol;
    }

    public String getSmplExt() {
        return smplExt;
    }

    public void setSmplExt(String smplExt) {
        this.smplExt = smplExt;
    }

    public String getSputmSampl() {
        return sputmSampl;
    }

    public void setSputmSampl(String sputmSampl) {
        this.sputmSampl = sputmSampl;
    }

    public String getnEnrollId() {
        return nEnrollId;
    }

    public void setnEnrollId(String nEnrollId) {
        this.nEnrollId = nEnrollId;
    }

    public String getnUserId() {
        return nUserId;
    }

    public void setnUserId(String nUserId) {
        this.nUserId = nUserId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
