package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomAddSample")
public class RoomAddSample {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "n_st_id")
    private String n_st_id;

    @ColumnInfo(name = "n_dis_id")
    private String n_dis_id;

    @ColumnInfo(name = "n_tu_id")
    private String n_tu_id;

    @ColumnInfo(name = "n_hf_id")
    private String n_hf_id;

    @ColumnInfo(name = "n_doc_idd")
    private String n_doc_idd;

    @ColumnInfo(name = "n_enroll_idd")
    private String n_enroll_idd;

    @ColumnInfo(name = "d_specm_coll")
    private String d_specm_coll;

    @ColumnInfo(name = "n_smpl_ext_idd")
    private String n_smpl_ext_idd;

    @ColumnInfo(name = "n_test_reas_idd")
    private String n_test_reas_idd;

    @ColumnInfo(name = "n_purp_vstt")
    private String n_purp_vstt;

    @ColumnInfo(name = "n_typ_specm_idd")
    private String n_typ_specm_idd;

    @ColumnInfo(name = "n_cont_smpll")
    private String n_cont_smpll;

    @ColumnInfo(name = "c_plc_samp_coll")
    private String c_plc_samp_coll;

    @ColumnInfo(name = "n_sputm_typ_idd")
    private String n_sputm_typ_idd;

    @ColumnInfo(name = "n_diag_tstt")
    private String n_diag_tstt;

    @ColumnInfo(name = "n_lab_idd")
    private String n_lab_idd;

    @ColumnInfo(name = "n_staff_infoo")
    private String n_staff_infoo;

    @ColumnInfo(name = "n_user_idd")
    private String n_user_idd;


    public RoomAddSample(String n_st_id, String n_dis_id, String n_tu_id, String n_hf_id, String n_doc_idd, String n_enroll_idd, String d_specm_coll, String n_smpl_ext_idd, String n_test_reas_idd, String n_purp_vstt, String n_typ_specm_idd, String n_cont_smpll, String c_plc_samp_coll, String n_sputm_typ_idd, String n_diag_tstt, String n_lab_idd, String n_staff_infoo, String n_user_idd) {
        this.n_st_id = n_st_id;
        this.n_dis_id = n_dis_id;
        this.n_tu_id = n_tu_id;
        this.n_hf_id = n_hf_id;
        this.n_doc_idd = n_doc_idd;
        this.n_enroll_idd = n_enroll_idd;
        this.d_specm_coll = d_specm_coll;
        this.n_smpl_ext_idd = n_smpl_ext_idd;
        this.n_test_reas_idd = n_test_reas_idd;
        this.n_purp_vstt = n_purp_vstt;
        this.n_typ_specm_idd = n_typ_specm_idd;
        this.n_cont_smpll = n_cont_smpll;
        this.c_plc_samp_coll = c_plc_samp_coll;
        this.n_sputm_typ_idd = n_sputm_typ_idd;
        this.n_diag_tstt = n_diag_tstt;
        this.n_lab_idd = n_lab_idd;
        this.n_staff_infoo = n_staff_infoo;
        this.n_user_idd = n_user_idd;
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

    public String getN_tu_id() {
        return n_tu_id;
    }

    public void setN_tu_id(String n_tu_id) {
        this.n_tu_id = n_tu_id;
    }

    public String getN_hf_id() {
        return n_hf_id;
    }

    public void setN_hf_id(String n_hf_id) {
        this.n_hf_id = n_hf_id;
    }

    public String getN_doc_idd() {
        return n_doc_idd;
    }

    public void setN_doc_idd(String n_doc_idd) {
        this.n_doc_idd = n_doc_idd;
    }

    public String getN_enroll_idd() {
        return n_enroll_idd;
    }

    public void setN_enroll_idd(String n_enroll_idd) {
        this.n_enroll_idd = n_enroll_idd;
    }

    public String getD_specm_coll() {
        return d_specm_coll;
    }

    public void setD_specm_coll(String d_specm_coll) {
        this.d_specm_coll = d_specm_coll;
    }

    public String getN_smpl_ext_idd() {
        return n_smpl_ext_idd;
    }

    public void setN_smpl_ext_idd(String n_smpl_ext_idd) {
        this.n_smpl_ext_idd = n_smpl_ext_idd;
    }

    public String getN_test_reas_idd() {
        return n_test_reas_idd;
    }

    public void setN_test_reas_idd(String n_test_reas_idd) {
        this.n_test_reas_idd = n_test_reas_idd;
    }

    public String getN_purp_vstt() {
        return n_purp_vstt;
    }

    public void setN_purp_vstt(String n_purp_vstt) {
        this.n_purp_vstt = n_purp_vstt;
    }

    public String getN_typ_specm_idd() {
        return n_typ_specm_idd;
    }

    public void setN_typ_specm_idd(String n_typ_specm_idd) {
        this.n_typ_specm_idd = n_typ_specm_idd;
    }

    public String getN_cont_smpll() {
        return n_cont_smpll;
    }

    public void setN_cont_smpll(String n_cont_smpll) {
        this.n_cont_smpll = n_cont_smpll;
    }

    public String getC_plc_samp_coll() {
        return c_plc_samp_coll;
    }

    public void setC_plc_samp_coll(String c_plc_samp_coll) {
        this.c_plc_samp_coll = c_plc_samp_coll;
    }

    public String getN_sputm_typ_idd() {
        return n_sputm_typ_idd;
    }

    public void setN_sputm_typ_idd(String n_sputm_typ_idd) {
        this.n_sputm_typ_idd = n_sputm_typ_idd;
    }

    public String getN_diag_tstt() {
        return n_diag_tstt;
    }

    public void setN_diag_tstt(String n_diag_tstt) {
        this.n_diag_tstt = n_diag_tstt;
    }

    public String getN_lab_idd() {
        return n_lab_idd;
    }

    public void setN_lab_idd(String n_lab_idd) {
        this.n_lab_idd = n_lab_idd;
    }

    public String getN_staff_infoo() {
        return n_staff_infoo;
    }

    public void setN_staff_infoo(String n_staff_infoo) {
        this.n_staff_infoo = n_staff_infoo;
    }

    public String getN_user_idd() {
        return n_user_idd;
    }

    public void setN_user_idd(String n_user_idd) {
        this.n_user_idd = n_user_idd;
    }

    public Integer getIds() {
        return ids;
    }
}
