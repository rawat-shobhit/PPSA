package com.smit.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestData {
    @SerializedName("d_smpl_recd")
    @Expose
    private String dSmplRecd;
    @SerializedName("d_rpt_doc")
    @Expose
    private String dRptDoc;
    @SerializedName("diag_test")
    @Expose
    private String diagTest;
    @SerializedName("d_tst_diag")
    @Expose
    private String dTstDiag;
    @SerializedName("d_tst_rpt_diag")
    @Expose
    private String dTstRptDiag;
    @SerializedName("dst_test")
    @Expose
    private String dstTest;
    @SerializedName("d_tst_dst")
    @Expose
    private String dTstDst;
    @SerializedName("d_tst_rpt_dst")
    @Expose
    private String dTstRptDst;
    @SerializedName("other_dst")
    @Expose
    private String otherDst;
    @SerializedName("d_tst_rpt_oth_dst")
    @Expose
    private String dTstRptOthDst;
    @SerializedName("d_tst_oth_dst")
    @Expose
    private String dTstOthDst;
    @SerializedName("fnl_interp")
    @Expose
    private String fnlInterp;
    @SerializedName("test_resul")
    @Expose
    private String testResul;
    @SerializedName("case_typ")
    @Expose
    private String caseTyp;
    @SerializedName("Patient_Status")
    @Expose
    private String patientStatus;
    @SerializedName("n_smpl_col_id")
    @Expose
    private String nSmplColId;
    @SerializedName("n_user_id")
    @Expose
    private String nUserId;

    public String getdSmplRecd() {
        return dSmplRecd;
    }

    public void setdSmplRecd(String dSmplRecd) {
        this.dSmplRecd = dSmplRecd;
    }

    public String getdRptDoc() {
        return dRptDoc;
    }

    public void setdRptDoc(String dRptDoc) {
        this.dRptDoc = dRptDoc;
    }

    public String getDiagTest() {
        return diagTest;
    }

    public void setDiagTest(String diagTest) {
        this.diagTest = diagTest;
    }

    public String getdTstDiag() {
        return dTstDiag;
    }

    public void setdTstDiag(String dTstDiag) {
        this.dTstDiag = dTstDiag;
    }

    public String getdTstRptDiag() {
        return dTstRptDiag;
    }

    public void setdTstRptDiag(String dTstRptDiag) {
        this.dTstRptDiag = dTstRptDiag;
    }

    public String getDstTest() {
        return dstTest;
    }

    public void setDstTest(String dstTest) {
        this.dstTest = dstTest;
    }

    public String getdTstDst() {
        return dTstDst;
    }

    public void setdTstDst(String dTstDst) {
        this.dTstDst = dTstDst;
    }

    public String getdTstRptDst() {
        return dTstRptDst;
    }

    public void setdTstRptDst(String dTstRptDst) {
        this.dTstRptDst = dTstRptDst;
    }

    public String getOtherDst() {
        return otherDst;
    }

    public void setOtherDst(String otherDst) {
        this.otherDst = otherDst;
    }

    public String getdTstRptOthDst() {
        return dTstRptOthDst;
    }

    public void setdTstRptOthDst(String dTstRptOthDst) {
        this.dTstRptOthDst = dTstRptOthDst;
    }

    public String getdTstOthDst() {
        return dTstOthDst;
    }

    public void setdTstOthDst(String dTstOthDst) {
        this.dTstOthDst = dTstOthDst;
    }

    public String getFnlInterp() {
        return fnlInterp;
    }

    public void setFnlInterp(String fnlInterp) {
        this.fnlInterp = fnlInterp;
    }

    public String getTestResul() {
        return testResul;
    }

    public void setTestResul(String testResul) {
        this.testResul = testResul;
    }

    public String getCaseTyp() {
        return caseTyp;
    }

    public void setCaseTyp(String caseTyp) {
        this.caseTyp = caseTyp;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getnSmplColId() {
        return nSmplColId;
    }

    public void setnSmplColId(String nSmplColId) {
        this.nSmplColId = nSmplColId;
    }

    public String getnUserId() {
        return nUserId;
    }

    public void setnUserId(String nUserId) {
        this.nUserId = nUserId;
    }
}
