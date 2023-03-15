package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomPreviousSamplesCollection")
public class RoomPreviousSamplesCollection {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "c_plc_samp_col")
    private String c_plc_samp_col;

    @ColumnInfo(name = "d_specm_col")
    private String d_specm_col;

    @ColumnInfo(name = "diag_test")
    private String diag_test;

    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "n_dis_id")
    private String n_dis_id;

    @ColumnInfo(name = "n_doc_id")
    private String n_doc_id;

    @ColumnInfo(name = "n_enroll_id")
    private String n_enroll_id;

    @ColumnInfo(name = "n_hf_id")
    private String n_hf_id;

    @ColumnInfo(name = "n_st_id")
    private String n_st_id;

    @ColumnInfo(name = "n_tu_id")
    private String n_tu_id;

    @ColumnInfo(name = "n_user_id")
    private String n_user_id;

    @ColumnInfo(name = "smpl_ext")
    private String smpl_ext;

    @ColumnInfo(name = "specm_typ")
    private String specm_typ;

    @ColumnInfo(name = "sputm_sampl")
    private String sputm_sampl;

    @ColumnInfo(name = "test_reas")
    private String test_reas;


    public RoomPreviousSamplesCollection(String c_plc_samp_col, String d_specm_col, String diag_test, String id, String n_dis_id, String n_doc_id, String n_enroll_id, String n_hf_id, String n_st_id, String n_tu_id, String n_user_id, String smpl_ext, String specm_typ, String sputm_sampl, String test_reas) {
        this.c_plc_samp_col = c_plc_samp_col;
        this.d_specm_col = d_specm_col;
        this.diag_test = diag_test;
        this.id = id;
        this.n_dis_id = n_dis_id;
        this.n_doc_id = n_doc_id;
        this.n_enroll_id = n_enroll_id;
        this.n_hf_id = n_hf_id;
        this.n_st_id = n_st_id;
        this.n_tu_id = n_tu_id;
        this.n_user_id = n_user_id;
        this.smpl_ext = smpl_ext;
        this.specm_typ = specm_typ;
        this.sputm_sampl = sputm_sampl;
        this.test_reas = test_reas;
    }

    public String getC_plc_samp_col() {
        return c_plc_samp_col;
    }

    public void setC_plc_samp_col(String c_plc_samp_col) {
        this.c_plc_samp_col = c_plc_samp_col;
    }

    public String getD_specm_col() {
        return d_specm_col;
    }

    public void setD_specm_col(String d_specm_col) {
        this.d_specm_col = d_specm_col;
    }

    public String getDiag_test() {
        return diag_test;
    }

    public void setDiag_test(String diag_test) {
        this.diag_test = diag_test;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getN_enroll_id() {
        return n_enroll_id;
    }

    public void setN_enroll_id(String n_enroll_id) {
        this.n_enroll_id = n_enroll_id;
    }

    public String getN_hf_id() {
        return n_hf_id;
    }

    public void setN_hf_id(String n_hf_id) {
        this.n_hf_id = n_hf_id;
    }

    public String getN_st_id() {
        return n_st_id;
    }

    public void setN_st_id(String n_st_id) {
        this.n_st_id = n_st_id;
    }

    public String getN_tu_id() {
        return n_tu_id;
    }

    public void setN_tu_id(String n_tu_id) {
        this.n_tu_id = n_tu_id;
    }

    public String getN_user_id() {
        return n_user_id;
    }

    public void setN_user_id(String n_user_id) {
        this.n_user_id = n_user_id;
    }

    public String getSmpl_ext() {
        return smpl_ext;
    }

    public void setSmpl_ext(String smpl_ext) {
        this.smpl_ext = smpl_ext;
    }

    public String getSpecm_typ() {
        return specm_typ;
    }

    public void setSpecm_typ(String specm_typ) {
        this.specm_typ = specm_typ;
    }

    public String getSputm_sampl() {
        return sputm_sampl;
    }

    public void setSputm_sampl(String sputm_sampl) {
        this.sputm_sampl = sputm_sampl;
    }

    public String getTest_reas() {
        return test_reas;
    }

    public void setTest_reas(String test_reas) {
        this.test_reas = test_reas;
    }

    public Integer getIds() {
        return ids;
    }
}
