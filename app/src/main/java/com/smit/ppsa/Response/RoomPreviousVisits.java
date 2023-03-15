package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomPreviousVisits")
public class RoomPreviousVisits {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "c_val")
    private String c_val;

    @ColumnInfo(name = "d_coun")
    private String d_coun;

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

    public RoomPreviousVisits(String c_val, String d_coun, String n_dis_id, String n_doc_id, String n_enroll_id, String n_hf_id, String n_st_id, String n_tu_id) {
        this.c_val = c_val;
        this.d_coun = d_coun;
        this.n_dis_id = n_dis_id;
        this.n_doc_id = n_doc_id;
        this.n_enroll_id = n_enroll_id;
        this.n_hf_id = n_hf_id;
        this.n_st_id = n_st_id;
        this.n_tu_id = n_tu_id;
    }

    public String getC_val() {
        return c_val;
    }

    public void setC_val(String c_val) {
        this.c_val = c_val;
    }

    public String getD_coun() {
        return d_coun;
    }

    public void setD_coun(String d_coun) {
        this.d_coun = d_coun;
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

    public Integer getIds() {
        return ids;
    }
}
