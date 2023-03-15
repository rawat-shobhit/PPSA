package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomPostProvider")
public class RoomPostProvider {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "n_st_idd")
    private String n_st_idd;

    @ColumnInfo(name = "n_dis_idd")
    private String n_dis_idd;

    @ColumnInfo(name = "n_tu_idd")
    private String n_tu_idd;

    @ColumnInfo(name = "n_hf_idd")
    private String n_hf_idd;

    @ColumnInfo(name = "typp")
    private String typp;

    @ColumnInfo(name = "d_visitt")
    private String d_visitt;

    @ColumnInfo(name = "n_visit_idd")
    private String n_visit_idd;

    @ColumnInfo(name = "n_visit_name")
    private String n_visit_name;

    @ColumnInfo(name = "n_sac_idd")
    private String n_sac_idd;

    @ColumnInfo(name = "n_user_idd")
    private String n_user_idd;


    public RoomPostProvider(String n_st_idd, String n_dis_idd, String n_tu_idd, String n_hf_idd, String typp, String d_visitt, String n_visit_idd, String n_sac_idd, String n_user_idd) {
        this.n_st_idd = n_st_idd;
        this.n_dis_idd = n_dis_idd;
        this.n_tu_idd = n_tu_idd;
        this.n_hf_idd = n_hf_idd;
        this.typp = typp;
        this.d_visitt = d_visitt;
        this.n_visit_idd = n_visit_idd;
        this.n_visit_name = n_visit_name;
        this.n_sac_idd = n_sac_idd;
        this.n_user_idd = n_user_idd;
    }

    public String getN_st_idd() {
        return n_st_idd;
    }

    public void setN_st_idd(String n_st_idd) {
        this.n_st_idd = n_st_idd;
    }

    public String getN_dis_idd() {
        return n_dis_idd;
    }

    public void setN_dis_idd(String n_dis_idd) {
        this.n_dis_idd = n_dis_idd;
    }

    public String getN_tu_idd() {
        return n_tu_idd;
    }

    public void setN_tu_idd(String n_tu_idd) {
        this.n_tu_idd = n_tu_idd;
    }

    public String getN_hf_idd() {
        return n_hf_idd;
    }

    public void setN_hf_idd(String n_hf_idd) {
        this.n_hf_idd = n_hf_idd;
    }

    public String getTypp() {
        return typp;
    }

    public void setTypp(String typp) {
        this.typp = typp;
    }

    public String getD_visitt() {
        return d_visitt;
    }

    public void setD_visitt(String d_visitt) {
        this.d_visitt = d_visitt;
    }

    public String getN_visit_idd() {
        return n_visit_idd;
    }

    public void setN_visit_idd(String n_visit_idd) {
        this.n_visit_idd = n_visit_idd;
    }

    public String getN_visit_name() {
        return n_visit_name;
    }

    public void setN_visit_name(String n_visit_name) {
        this.n_visit_name = n_visit_name;
    }

    public String getN_sac_idd() {
        return n_sac_idd;
    }

    public void setN_sac_idd(String n_sac_idd) {
        this.n_sac_idd = n_sac_idd;
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
