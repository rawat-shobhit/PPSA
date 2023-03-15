package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomPreviousSamplesHf")
public class RoomPreviousSamplesHf {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "d_issue")
    private String d_issue;

    @ColumnInfo(name = "n_dis_id")
    private String n_dis_id;

    @ColumnInfo(name = "n_doc_id")
    private String n_doc_id;

    @ColumnInfo(name = "n_hf_id")
    private String n_hf_id;

    @ColumnInfo(name = "n_st_id")
    private String n_st_id;

    @ColumnInfo(name = "n_tu_id")
    private String n_tu_id;

    @ColumnInfo(name = "tot_drug")
    private String tot_drug;


    public RoomPreviousSamplesHf(String d_issue, String n_dis_id, String n_doc_id, String n_hf_id, String n_st_id, String n_tu_id, String tot_drug) {
        this.d_issue = d_issue;
        this.n_dis_id = n_dis_id;
        this.n_doc_id = n_doc_id;
        this.n_hf_id = n_hf_id;
        this.n_st_id = n_st_id;
        this.n_tu_id = n_tu_id;
        this.tot_drug = tot_drug;
    }

    public String getD_issue() {
        return d_issue;
    }

    public void setD_issue(String d_issue) {
        this.d_issue = d_issue;
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

    public String getTot_drug() {
        return tot_drug;
    }

    public void setTot_drug(String tot_drug) {
        this.tot_drug = tot_drug;
    }

    public Integer getIds() {
        return ids;
    }
}
