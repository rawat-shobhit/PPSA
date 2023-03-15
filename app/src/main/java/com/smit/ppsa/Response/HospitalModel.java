package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HospitalModel {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    @ColumnInfo(name = "stID")
    private String stID;
    @ColumnInfo(name = "disID")
    private String disID;
    @ColumnInfo(name = "n_tu_id")
    private String n_tu_id;
    @ColumnInfo(name = "n_hf_cd")
    private String n_hf_cd;
    @ColumnInfo(name = "c_hf_nam")
    private String c_hf_nam;
    @ColumnInfo(name = "n_hf_typ_id")
    private String n_hf_typ_id;
    @ColumnInfo(name = "c_hf_addr")
    private String c_hf_addr;
    @ColumnInfo(name = "c_cont_per")
    private String c_cont_per;
    @ColumnInfo(name = "c_cp_mob")
    private String c_cp_mob;
    @ColumnInfo(name = "c_cp_email")
    private String c_cp_email;
    @ColumnInfo(name = "n_sc_id")
    private String n_sc_id;
    @ColumnInfo(name = "n_pp_idenr")
    private String n_pp_idenr;
    @ColumnInfo(name = "c_tc_nam")
    private String c_tc_nam;
    @ColumnInfo(name = "c_tc_mob")
    private String c_tc_mob;
    @ColumnInfo(name = "n_bf_id")
    private String n_bf_id;
    @ColumnInfo(name = "n_pay_status")
    private String n_pay_status;
    @ColumnInfo(name = "n_user_id")
    private String n_user_id;
    @ColumnInfo(name = "lat")
    private String lat;
    @ColumnInfo(name = "lng")
    private String lng;

    public HospitalModel(String stID, String disID, String n_tu_id, String n_hf_cd, String c_hf_nam, String n_hf_typ_id, String c_hf_addr, String c_cont_per, String c_cp_mob, String c_cp_email, String n_sc_id, String n_pp_idenr, String c_tc_nam, String c_tc_mob, String n_bf_id, String n_pay_status, String n_user_id, String lat, String lng) {
        this.stID = stID;
        this.disID = disID;
        this.n_tu_id = n_tu_id;
        this.n_hf_cd = n_hf_cd;
        this.c_hf_nam = c_hf_nam;
        this.n_hf_typ_id = n_hf_typ_id;
        this.c_hf_addr = c_hf_addr;
        this.c_cont_per = c_cont_per;
        this.c_cp_mob = c_cp_mob;
        this.c_cp_email = c_cp_email;
        this.n_sc_id = n_sc_id;
        this.n_pp_idenr = n_pp_idenr;
        this.c_tc_nam = c_tc_nam;
        this.c_tc_mob = c_tc_mob;
        this.n_bf_id = n_bf_id;
        this.n_pay_status = n_pay_status;
        this.n_user_id = n_user_id;
        this.lat = lat;
        this.lng = lng;
    }

    public String getStID() {
        return stID;
    }

    public String getDisID() {
        return disID;
    }

    public String getN_tu_id() {
        return n_tu_id;
    }

    public String getN_hf_cd() {
        return n_hf_cd;
    }

    public String getC_hf_nam() {
        return c_hf_nam;
    }

    public String getN_hf_typ_id() {
        return n_hf_typ_id;
    }

    public String getC_hf_addr() {
        return c_hf_addr;
    }

    public String getC_cont_per() {
        return c_cont_per;
    }

    public String getC_cp_mob() {
        return c_cp_mob;
    }

    public String getC_cp_email() {
        return c_cp_email;
    }

    public String getN_sc_id() {
        return n_sc_id;
    }

    public String getN_pp_idenr() {
        return n_pp_idenr;
    }

    public String getC_tc_nam() {
        return c_tc_nam;
    }

    public String getC_tc_mob() {
        return c_tc_mob;
    }

    public String getN_bf_id() {
        return n_bf_id;
    }

    public String getN_pay_status() {
        return n_pay_status;
    }

    public String getN_user_id() {
        return n_user_id;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
