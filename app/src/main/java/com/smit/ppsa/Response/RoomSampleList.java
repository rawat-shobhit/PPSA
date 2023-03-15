package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class RoomSampleList {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "test_reas")
    public String testReas;
    @ColumnInfo(name = "diag_test")
    public String diag_test;
    @ColumnInfo(name = "specm_typ")
    public String specmTyp;
    @ColumnInfo(name = "d_specm_col")
    public String dSpecmCol;
    @ColumnInfo(name = "c_plc_samp_col")
    public String cPlcSampCol;
    @ColumnInfo(name = "smpl_ext")
    public String smplExt;
    @ColumnInfo(name = "sputm_sampl")
    public String sputmSampl;
    @ColumnInfo(name = "n_enroll_id")
    public String nEnrollId;
    @ColumnInfo(name = "n_user_id")
    public String nUserId;
    @ColumnInfo(name = "n_st_id")
    public String nStId;
    @ColumnInfo(name = "n_dis_id")
    public String nDisId;
    @ColumnInfo(name = "n_tu_id")
    public String nTuId;
    @ColumnInfo(name = "n_hf_id")
    public String nHfId;
    @ColumnInfo(name = "id")
    private String id;

    public RoomSampleList(String diag_test, String testReas, String specmTyp, String dSpecmCol, String cPlcSampCol, String smplExt, String sputmSampl, String nEnrollId, String nUserId, String nStId, String nDisId, String nTuId, String nHfId, String id) {
        this.diag_test = diag_test;
        this.testReas = testReas;
        this.specmTyp = specmTyp;
        this.dSpecmCol = dSpecmCol;
        this.cPlcSampCol = cPlcSampCol;
        this.smplExt = smplExt;
        this.sputmSampl = sputmSampl;
        this.nEnrollId = nEnrollId;
        this.nUserId = nUserId;
        this.nStId = nStId;
        this.nDisId = nDisId;
        this.nTuId = nTuId;
        this.nHfId = nHfId;
        this.id = id;
    }

    public String getDiag_test() {
        return diag_test;
    }

    public void setDiag_test(String diag_test) {
        this.diag_test = diag_test;
    }

    public String getnStId() {
        return nStId;
    }

    public String getnDisId() {
        return nDisId;
    }

    public String getnTuId() {
        return nTuId;
    }

    public String getnHfId() {
        return nHfId;
    }

    public String getId() {
        return id;
    }

    public Integer getIds() {
        return ids;
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

    public String getnEnrollId() {
        return nEnrollId;
    }

    public String getnUserId() {
        return nUserId;
    }
}
