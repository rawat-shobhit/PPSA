package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomFormSixData")
public class RoomFormSixData {
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

    @ColumnInfo(name = "n_doc_id")
    private String n_doc_id;

    @ColumnInfo(name = "n_enroll_id")
    private String n_enroll_id;

    @ColumnInfo(name = "n_smpl_col_id")
    private String n_smpl_col_id;

    @ColumnInfo(name = "d_tst_rslt")
    private String d_tst_rslt;

    @ColumnInfo(name = "n_tst_rpt")
    private String n_tst_rpt;

    @ColumnInfo(name = "d_rpt_col")
    private String d_rpt_col;

    @ColumnInfo(name = "c_tr_fp_img")
    private String c_tr_fp_img;

    @ColumnInfo(name = "c_tr_bp_img")
    private String c_tr_bp_img;

    @ColumnInfo(name = "d_lpa_smpl")
    private String d_lpa_smpl;

    @ColumnInfo(name = "n_lpa_rslt")
    private String n_lpa_rslt;

    @ColumnInfo(name = "n_lat")
    private String n_lat;

    @ColumnInfo(name = "n_lng")
    private String n_lng;

    @ColumnInfo(name = "n_staff_info")
    private String n_staff_info;

    @ColumnInfo(name = "n_user_id")
    private String n_user_id;

    @ColumnInfo(name = "n_lab_id")
    private String n_lab_id;

    public RoomFormSixData(String n_st_id, String n_dis_id, String n_tu_id, String n_hf_id, String n_doc_id, String n_enroll_id, String n_smpl_col_id, String d_tst_rslt, String n_tst_rpt, String d_rpt_col, String c_tr_fp_img, String c_tr_bp_img, String d_lpa_smpl, String n_lpa_rslt, String n_lat, String n_lng, String n_staff_info, String n_user_id, String n_lab_id) {
        this.n_st_id = n_st_id;
        this.n_dis_id = n_dis_id;
        this.n_tu_id = n_tu_id;
        this.n_hf_id = n_hf_id;
        this.n_doc_id = n_doc_id;
        this.n_enroll_id = n_enroll_id;
        this.n_smpl_col_id = n_smpl_col_id;
        this.d_tst_rslt = d_tst_rslt;
        this.n_tst_rpt = n_tst_rpt;
        this.d_rpt_col = d_rpt_col;
        this.c_tr_fp_img = c_tr_fp_img;
        this.c_tr_bp_img = c_tr_bp_img;
        this.d_lpa_smpl = d_lpa_smpl;
        this.n_lpa_rslt = n_lpa_rslt;
        this.n_lat = n_lat;
        this.n_lng = n_lng;
        this.n_staff_info = n_staff_info;
        this.n_user_id = n_user_id;
        this.n_lab_id = n_lab_id;
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

    public String getN_smpl_col_id() {
        return n_smpl_col_id;
    }

    public void setN_smpl_col_id(String n_smpl_col_id) {
        this.n_smpl_col_id = n_smpl_col_id;
    }

    public String getD_tst_rslt() {
        return d_tst_rslt;
    }

    public void setD_tst_rslt(String d_tst_rslt) {
        this.d_tst_rslt = d_tst_rslt;
    }

    public String getN_tst_rpt() {
        return n_tst_rpt;
    }

    public void setN_tst_rpt(String n_tst_rpt) {
        this.n_tst_rpt = n_tst_rpt;
    }

    public String getD_rpt_col() {
        return d_rpt_col;
    }

    public void setD_rpt_col(String d_rpt_col) {
        this.d_rpt_col = d_rpt_col;
    }

    public String getC_tr_fp_img() {
        return c_tr_fp_img;
    }

    public void setC_tr_fp_img(String c_tr_fp_img) {
        this.c_tr_fp_img = c_tr_fp_img;
    }

    public String getC_tr_bp_img() {
        return c_tr_bp_img;
    }

    public void setC_tr_bp_img(String c_tr_bp_img) {
        this.c_tr_bp_img = c_tr_bp_img;
    }

    public String getD_lpa_smpl() {
        return d_lpa_smpl;
    }

    public void setD_lpa_smpl(String d_lpa_smpl) {
        this.d_lpa_smpl = d_lpa_smpl;
    }

    public String getN_lpa_rslt() {
        return n_lpa_rslt;
    }

    public void setN_lpa_rslt(String n_lpa_rslt) {
        this.n_lpa_rslt = n_lpa_rslt;
    }

    public String getN_lat() {
        return n_lat;
    }

    public void setN_lat(String n_lat) {
        this.n_lat = n_lat;
    }

    public String getN_lng() {
        return n_lng;
    }

    public void setN_lng(String n_lng) {
        this.n_lng = n_lng;
    }

    public String getN_staff_info() {
        return n_staff_info;
    }

    public void setN_staff_info(String n_staff_info) {
        this.n_staff_info = n_staff_info;
    }

    public String getN_user_id() {
        return n_user_id;
    }

    public void setN_user_id(String n_user_id) {
        this.n_user_id = n_user_id;
    }

    public String getN_lab_id() {
        return n_lab_id;
    }

    public void setN_lab_id(String n_lab_id) {
        this.n_lab_id = n_lab_id;
    }

    public Integer getIds() {
        return ids;
    }
}
