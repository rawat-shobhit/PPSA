package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomTestData {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "d_smpl_recd")
    public String dSmplRecd;
    @ColumnInfo(name = "d_rpt_doc")
    public String dRptDoc;
    @ColumnInfo(name = "diag_test")
    public String diagTest;
    @ColumnInfo(name = "d_tst_diag")
    public String dTstDiag;
    @ColumnInfo(name = "d_tst_rpt_diag")
    public String dTstRptDiag;
    @ColumnInfo(name = "dst_test")
    public String dstTest;
    @ColumnInfo(name = "d_tst_dst")
    public String dTstDst;
    @ColumnInfo(name = "d_tst_rpt_dst")
    public String dTstRptDst;
    @ColumnInfo(name = "other_dst")
    public String otherDst;
    @ColumnInfo(name = "d_tst_rpt_oth_dst")
    public String dTstRptOthDst;
    @ColumnInfo(name = "d_tst_oth_dst")
    public String dTstOthDst;
    @ColumnInfo(name = "fnl_interp")
    public String fnlInterp;
    @ColumnInfo(name = "test_resul")
    public String testResul;
    @ColumnInfo(name = "case_typ")
    public String caseTyp;
    @ColumnInfo(name = "Patient_Status")
    public String patientStatus;
    @ColumnInfo(name = "n_smpl_col_id")
    public String nSmplColId;
    @ColumnInfo(name = "n_user_id")
    public String nUserId;


    public RoomTestData(String dSmplRecd, String dRptDoc, String diagTest, String dTstDiag, String dTstRptDiag, String dstTest, String dTstDst, String dTstRptDst, String otherDst, String dTstRptOthDst, String dTstOthDst, String fnlInterp, String testResul, String caseTyp, String patientStatus, String nSmplColId, String nUserId) {
        this.dSmplRecd = dSmplRecd;
        this.dRptDoc = dRptDoc;
        this.diagTest = diagTest;
        this.dTstDiag = dTstDiag;
        this.dTstRptDiag = dTstRptDiag;
        this.dstTest = dstTest;
        this.dTstDst = dTstDst;
        this.dTstRptDst = dTstRptDst;
        this.otherDst = otherDst;
        this.dTstRptOthDst = dTstRptOthDst;
        this.dTstOthDst = dTstOthDst;
        this.fnlInterp = fnlInterp;
        this.testResul = testResul;
        this.caseTyp = caseTyp;
        this.patientStatus = patientStatus;
        this.nSmplColId = nSmplColId;
        this.nUserId = nUserId;
    }

    public Integer getIds() {
        return ids;
    }

    public String getdSmplRecd() {
        return dSmplRecd;
    }

    public String getdRptDoc() {
        return dRptDoc;
    }

    public String getDiagTest() {
        return diagTest;
    }

    public String getdTstDiag() {
        return dTstDiag;
    }

    public String getdTstRptDiag() {
        return dTstRptDiag;
    }

    public String getDstTest() {
        return dstTest;
    }

    public String getdTstDst() {
        return dTstDst;
    }

    public String getdTstRptDst() {
        return dTstRptDst;
    }

    public String getOtherDst() {
        return otherDst;
    }

    public String getdTstRptOthDst() {
        return dTstRptOthDst;
    }

    public String getdTstOthDst() {
        return dTstOthDst;
    }

    public String getFnlInterp() {
        return fnlInterp;
    }

    public String getTestResul() {
        return testResul;
    }

    public String getCaseTyp() {
        return caseTyp;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public String getnSmplColId() {
        return nSmplColId;
    }

    public String getnUserId() {
        return nUserId;
    }
}
