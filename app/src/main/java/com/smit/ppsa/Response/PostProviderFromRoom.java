package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PostProviderFromRoom {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "n_st_id")
    public String n_st_id;
    @ColumnInfo(name = "n_dis_id")
    public String n_dis_id;
    @ColumnInfo(name = "n_tu_id")
    public String n_tu_id;
    @ColumnInfo(name = "n_hf_id")
    public String n_hf_id;
    @ColumnInfo(name = "d_reg_dat")
    public String d_reg_dat;
    @ColumnInfo(name = "n_typ")
    public String n_typ;
    @ColumnInfo(name = "user_id")
    public String user_id;
    @ColumnInfo(name = "lat")
    public String lat;
    @ColumnInfo(name = "lng")
    public String lng;

    public PostProviderFromRoom(String n_st_id, String n_dis_id, String n_tu_id, String n_hf_id, String d_reg_dat, String n_typ, String user_id, String lat, String lng) {
        this.n_st_id = n_st_id;
        this.n_dis_id = n_dis_id;
        this.n_tu_id = n_tu_id;
        this.n_hf_id = n_hf_id;
        this.d_reg_dat = d_reg_dat;
        this.n_typ = n_typ;
        this.user_id = user_id;
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getIds() {
        return ids;
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

    public String getD_reg_dat() {
        return d_reg_dat;
    }

    public String getN_typ() {
        return n_typ;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
