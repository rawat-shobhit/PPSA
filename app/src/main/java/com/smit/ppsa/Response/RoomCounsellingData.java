package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomCounsellingData")
public class RoomCounsellingData {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "user_idd")
    private String user_idd;

    @ColumnInfo(name = "staff_infoo")
    private String staff_infoo;

    @ColumnInfo(name = "n_stidd")
    private String n_stidd;

    @ColumnInfo(name = "n_disIdd")
    private String n_disIdd;

    @ColumnInfo(name = "n_docIdd")
    private String n_docIdd;

    @ColumnInfo(name = "latt")
    private String latt;

    @ColumnInfo(name = "lngg")
    private String lngg;

    @ColumnInfo(name = "n_typeCounn")
    private String n_typeCounn;

    @ColumnInfo(name = "d_Counn")
    private String d_Counn;

    @ColumnInfo(name = "neenrollIDD")
    private String neenrollIDD;

    @ColumnInfo(name = "n_TuIdd")
    private String n_TuIdd;

    @ColumnInfo(name = "n_Hfidd")
    private String n_Hfidd;

    public RoomCounsellingData(String user_idd, String staff_infoo, String n_stidd, String n_disIdd, String n_docIdd, String latt, String lngg, String n_typeCounn, String d_Counn, String neenrollIDD, String n_TuIdd, String n_Hfidd) {
        this.user_idd = user_idd;
        this.staff_infoo = staff_infoo;
        this.n_stidd = n_stidd;
        this.n_disIdd = n_disIdd;
        this.n_docIdd = n_docIdd;
        this.latt = latt;
        this.lngg = lngg;
        this.n_typeCounn = n_typeCounn;
        this.d_Counn = d_Counn;
        this.neenrollIDD = neenrollIDD;
        this.n_TuIdd = n_TuIdd;
        this.n_Hfidd = n_Hfidd;
    }

    public String getUser_idd() {
        return user_idd;
    }

    public void setUser_idd(String user_idd) {
        this.user_idd = user_idd;
    }

    public String getStaff_infoo() {
        return staff_infoo;
    }

    public void setStaff_infoo(String staff_infoo) {
        this.staff_infoo = staff_infoo;
    }

    public String getN_stidd() {
        return n_stidd;
    }

    public void setN_stidd(String n_stidd) {
        this.n_stidd = n_stidd;
    }

    public String getN_disIdd() {
        return n_disIdd;
    }

    public void setN_disIdd(String n_disIdd) {
        this.n_disIdd = n_disIdd;
    }

    public String getN_docIdd() {
        return n_docIdd;
    }

    public void setN_docIdd(String n_docIdd) {
        this.n_docIdd = n_docIdd;
    }

    public String getLatt() {
        return latt;
    }

    public void setLatt(String latt) {
        this.latt = latt;
    }

    public String getLngg() {
        return lngg;
    }

    public void setLngg(String lngg) {
        this.lngg = lngg;
    }

    public String getN_typeCounn() {
        return n_typeCounn;
    }

    public void setN_typeCounn(String n_typeCounn) {
        this.n_typeCounn = n_typeCounn;
    }

    public String getD_Counn() {
        return d_Counn;
    }

    public void setD_Counn(String d_Counn) {
        this.d_Counn = d_Counn;
    }

    public String getNeenrollIDD() {
        return neenrollIDD;
    }

    public void setNeenrollIDD(String neenrollIDD) {
        this.neenrollIDD = neenrollIDD;
    }

    public String getN_TuIdd() {
        return n_TuIdd;
    }

    public void setN_TuIdd(String n_TuIdd) {
        this.n_TuIdd = n_TuIdd;
    }

    public String getN_Hfidd() {
        return n_Hfidd;
    }

    public void setN_Hfidd(String n_Hfidd) {
        this.n_Hfidd = n_Hfidd;
    }

    public Integer getIds() {
        return ids;
    }
}
