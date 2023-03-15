package com.smit.ppsa;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FdcReceivedModel {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "d_rec")
    private String d_rec;

    @ColumnInfo(name = "n_med")
    private String n_med;

    @ColumnInfo(name = "n_uom")
    private String n_uom;

    @ColumnInfo(name = "n_qty")
    private String n_qty;

    @ColumnInfo(name = "n_sanc")
    private String n_sanc;

    @ColumnInfo(name = "n_lat")
    private String n_lat;

    @ColumnInfo(name = "n_lng")
    private String n_lng;

    @ColumnInfo(name = "n_staff_info")
    private String n_staff_info;

    @ColumnInfo(name = "n_user_id")
    private String n_user_id;

    public FdcReceivedModel(String d_rec, String n_med, String n_uom, String n_qty, String n_sanc, String n_lat, String n_lng, String n_staff_info, String n_user_id) {
        this.d_rec = d_rec;
        this.n_med = n_med;
        this.n_uom = n_uom;
        this.n_qty = n_qty;
        this.n_sanc = n_sanc;
        this.n_lat = n_lat;
        this.n_lng = n_lng;
        this.n_staff_info = n_staff_info;
        this.n_user_id = n_user_id;
    }

    public String getD_rec() {
        return d_rec;
    }

    public void setD_rec(String d_rec) {
        this.d_rec = d_rec;
    }

    public String getN_med() {
        return n_med;
    }

    public void setN_med(String n_med) {
        this.n_med = n_med;
    }

    public String getN_uom() {
        return n_uom;
    }

    public void setN_uom(String n_uom) {
        this.n_uom = n_uom;
    }

    public String getN_qty() {
        return n_qty;
    }

    public void setN_qty(String n_qty) {
        this.n_qty = n_qty;
    }

    public String getN_sanc() {
        return n_sanc;
    }

    public void setN_sanc(String n_sanc) {
        this.n_sanc = n_sanc;
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

    public Integer getId() {
        return id;
    }
}
