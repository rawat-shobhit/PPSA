package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomFdcOpenStockBalance")
public class RoomFdcOpenStockBalance {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "n_st_id")
    private int n_st_id;

    @ColumnInfo(name = "n_dis_id")
    private int n_dis_id;

    @ColumnInfo(name = "n_tu_id")
    private int n_tu_id;

    @ColumnInfo(name = "n_hf_id")
    private int n_hf_id;


    @ColumnInfo(name = "n_fdc2")
    private int n_fdc2;

    @ColumnInfo(name = "n_fdc3")
    private int n_fdc3;

    @ColumnInfo(name = "n_fdc4")
    private int n_fdc4;

    @ColumnInfo(name = "n_etham")
    private int n_etham;

    @ColumnInfo(name = "n_lat")
    private String n_lat;

    @ColumnInfo(name = "n_lng")
    private String n_lng;

    @ColumnInfo(name = "n_staff_info")
    private int n_staff_info;

    @ColumnInfo(name = "n_user_id")
    private int n_user_id;

    public RoomFdcOpenStockBalance(int n_st_id, int n_dis_id, int n_tu_id, int n_hf_id, int n_fdc2, int n_fdc3, int n_fdc4, int n_etham, String n_lat, String n_lng, int n_staff_info, int n_user_id) {
        this.n_st_id = n_st_id;
        this.n_dis_id = n_dis_id;
        this.n_tu_id = n_tu_id;
        this.n_hf_id = n_hf_id;
        this.n_fdc2 = n_fdc2;
        this.n_fdc3 = n_fdc3;
        this.n_fdc4 = n_fdc4;
        this.n_etham = n_etham;
        this.n_lat = n_lat;
        this.n_lng = n_lng;
        this.n_staff_info = n_staff_info;
        this.n_user_id = n_user_id;
    }

    public int getN_st_id() {
        return n_st_id;
    }

    public void setN_st_id(int n_st_id) {
        this.n_st_id = n_st_id;
    }

    public int getN_dis_id() {
        return n_dis_id;
    }

    public void setN_dis_id(int n_dis_id) {
        this.n_dis_id = n_dis_id;
    }

    public int getN_tu_id() {
        return n_tu_id;
    }

    public void setN_tu_id(int n_tu_id) {
        this.n_tu_id = n_tu_id;
    }

    public int getN_hf_id() {
        return n_hf_id;
    }

    public void setN_hf_id(int n_hf_id) {
        this.n_hf_id = n_hf_id;
    }

    public int getN_fdc2() {
        return n_fdc2;
    }

    public void setN_fdc2(int n_fdc2) {
        this.n_fdc2 = n_fdc2;
    }

    public int getN_fdc3() {
        return n_fdc3;
    }

    public void setN_fdc3(int n_fdc3) {
        this.n_fdc3 = n_fdc3;
    }

    public int getN_fdc4() {
        return n_fdc4;
    }

    public void setN_fdc4(int n_fdc4) {
        this.n_fdc4 = n_fdc4;
    }

    public int getN_etham() {
        return n_etham;
    }

    public void setN_etham(int n_etham) {
        this.n_etham = n_etham;
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

    public int getN_staff_info() {
        return n_staff_info;
    }

    public void setN_staff_info(int n_staff_info) {
        this.n_staff_info = n_staff_info;
    }

    public int getN_user_id() {
        return n_user_id;
    }

    public void setN_user_id(int n_user_id) {
        this.n_user_id = n_user_id;
    }

    public Integer getIds() {
        return ids;
    }
}
