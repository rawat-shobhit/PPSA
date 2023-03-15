package com.example.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FormOneModel {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
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
    @ColumnInfo(name = "d_reg_dat")
    private String d_reg_dat;
    @ColumnInfo(name = "n_nksh_id")
    private String n_nksh_id;
    @ColumnInfo(name = "c_pat_nam")
    private String c_pat_nam;
    @ColumnInfo(name = "n_age")
    private String n_age;
    @ColumnInfo(name = "n_sex")
    private String n_sex;
    @ColumnInfo(name = "n_wght")
    private String n_wght;
    @ColumnInfo(name = "n_hght")
    private String n_hght;
    @ColumnInfo(name = "c_add")
    private String c_add;
    @ColumnInfo(name = "c_taluka")
    private String c_taluka;
    @ColumnInfo(name = "c_town")
    private String c_town;
    @ColumnInfo(name = "c_ward")
    private String c_ward;
    @ColumnInfo(name = "c_lnd_mrk")
    private String c_lnd_mrk;
    @ColumnInfo(name = "n_pin")
    private String n_pin;
    @ColumnInfo(name = "n_st_id_res")
    private String n_st_id_res;
    @ColumnInfo(name = "n_dis_id_res")
    private String n_dis_id_res;
    @ColumnInfo(name = "n_tu_id_res")
    private String n_tu_id_res;
    @ColumnInfo(name = "c_mob")
    private String c_mob;
    @ColumnInfo(name = "c_mob_2")
    private String c_mob_2;
    @ColumnInfo(name = "n_lat")
    private String n_lat;
    @ColumnInfo(name = "n_lng")
    private String n_lng;
    @ColumnInfo(name = "n_user_id")
    private String n_user_id;


    public FormOneModel(String n_st_id, String n_dis_id, String n_tu_id, String n_hf_id, String n_doc_id, String d_reg_dat, String n_nksh_id, String c_pat_nam, String n_age, String n_sex, String n_wght, String n_hght, String c_add, String c_taluka, String c_town, String c_ward, String c_lnd_mrk, String n_pin, String n_st_id_res, String n_dis_id_res, String n_tu_id_res, String c_mob, String c_mob_2, String n_lat, String n_lng, String n_user_id) {
        this.n_st_id = n_st_id;
        this.n_dis_id = n_dis_id;
        this.n_tu_id = n_tu_id;
        this.n_hf_id = n_hf_id;
        this.n_doc_id = n_doc_id;
        this.d_reg_dat = d_reg_dat;
        this.n_nksh_id = n_nksh_id;
        this.c_pat_nam = c_pat_nam;
        this.n_age = n_age;
        this.n_sex = n_sex;
        this.n_wght = n_wght;
        this.n_hght = n_hght;
        this.c_add = c_add;
        this.c_taluka = c_taluka;
        this.c_town = c_town;
        this.c_ward = c_ward;
        this.c_lnd_mrk = c_lnd_mrk;
        this.n_pin = n_pin;
        this.n_st_id_res = n_st_id_res;
        this.n_dis_id_res = n_dis_id_res;
        this.n_tu_id_res = n_tu_id_res;
        this.c_mob = c_mob;
        this.c_mob_2 = c_mob_2;
        this.n_lat = n_lat;
        this.n_lng = n_lng;
        this.n_user_id = n_user_id;
    }

    public Integer getId() {
        return id;
    }

    public String getN_st_id() {
        return n_st_id;
    }

    public String getN_dis_id() {
        return n_dis_id;
    }

    public String getN_tu_id() {
        return n_tu_id;
    }

    public String getN_hf_id() {
        return n_hf_id;
    }

    public String getN_doc_id() {
        return n_doc_id;
    }

    public String getD_reg_dat() {
        return d_reg_dat;
    }

    public String getN_nksh_id() {
        return n_nksh_id;
    }

    public String getC_pat_nam() {
        return c_pat_nam;
    }

    public String getN_age() {
        return n_age;
    }

    public String getN_sex() {
        return n_sex;
    }

    public String getN_wght() {
        return n_wght;
    }

    public String getN_hght() {
        return n_hght;
    }

    public String getC_add() {
        return c_add;
    }

    public String getC_taluka() {
        return c_taluka;
    }

    public String getC_town() {
        return c_town;
    }

    public String getC_ward() {
        return c_ward;
    }

    public String getC_lnd_mrk() {
        return c_lnd_mrk;
    }

    public String getN_pin() {
        return n_pin;
    }

    public String getN_st_id_res() {
        return n_st_id_res;
    }

    public String getN_dis_id_res() {
        return n_dis_id_res;
    }

    public String getN_tu_id_res() {
        return n_tu_id_res;
    }

    public String getC_mob() {
        return c_mob;
    }

    public String getC_mob_2() {
        return c_mob_2;
    }

    public String getN_lat() {
        return n_lat;
    }

    public String getN_lng() {
        return n_lng;
    }

    public String getN_user_id() {
        return n_user_id;
    }
}
