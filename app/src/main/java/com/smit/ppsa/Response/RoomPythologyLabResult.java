package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomPythologyLabResult")
public class RoomPythologyLabResult {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;

    @ColumnInfo(name = "all_code")
    private String all_code;

    @ColumnInfo(name = "all_short")
    private String all_short;

    @ColumnInfo(name = "c_cont_per")
    private String c_cont_per;

    @ColumnInfo(name = "c_cp_email")
    private String c_cp_email;

    @ColumnInfo(name = "c_cp_mob")
    private String c_cp_mob;

    @ColumnInfo(name = "c_dis_nam")
    private String c_dis_nam;

    @ColumnInfo(name = "c_dis_short")
    private String c_dis_short;

    @ColumnInfo(name = "c_hf_addr")
    private String c_hf_addr;

    @ColumnInfo(name = "c_hf_nam")
    private String c_hf_nam;

    @ColumnInfo(name = "c_hf_typ")
    private String c_hf_typ;

    @ColumnInfo(name = "c_st_nam")
    private String c_st_nam;

    @ColumnInfo(name = "c_st_short")
    private String c_st_short;

    @ColumnInfo(name = "c_tu_name")
    private String c_tu_name;

    @ColumnInfo(name = "n_dis_id")
    private String n_dis_id;

    @ColumnInfo(name = "n_hf_cd")
    private String n_hf_cd;

    @ColumnInfo(name = "n_hf_id")
    private String n_hf_id;

    @ColumnInfo(name = "n_hf_typ_id")
    private String n_hf_typ_id;

    @ColumnInfo(name = "n_oth_sanc_id")
    private String n_oth_sanc_id;

    @ColumnInfo(name = "n_pc_sanc_id")
    private String n_pc_sanc_id;

    @ColumnInfo(name = "n_pm_sanc_id")
    private String n_pm_sanc_id;

    @ColumnInfo(name = "n_st_id")
    private String n_st_id;

    @ColumnInfo(name = "n_stf_sanc_id")
    private String n_stf_sanc_id;
    @ColumnInfo(name = "n_tu_id")
    private String n_tu_id;

    @ColumnInfo(name = "oth_lng")
    private String oth_lng;

    @ColumnInfo(name = "oth_nam")
    private String oth_nam;

    @ColumnInfo(name = "oth_staff_id")
    private String oth_staff_id;

    @ColumnInfo(name = "pc_code")
    private String pc_code;

    @ColumnInfo(name = "pc_lng")
    private String pc_lng;

    @ColumnInfo(name = "pc_nam")
    private String pc_nam;

    @ColumnInfo(name = "pc_short")
    private String pc_short;

    @ColumnInfo(name = "pc_staff_id")
    private String pc_staff_id;

    @ColumnInfo(name = "pm_code")
    private String pm_code;

    @ColumnInfo(name = "pm_lng")
    private String pm_lng;

    @ColumnInfo(name = "pm_nam")
    private String pm_nam;

    @ColumnInfo(name = "pm_short")
    private String pm_short;

    @ColumnInfo(name = "pm_staff_id")
    private String pm_staff_id;

    @ColumnInfo(name = "stf_code")
    private String stf_code;

    @ColumnInfo(name = "stf_lng")
    private String stf_lng;

    @ColumnInfo(name = "stf_nam")
    private String stf_nam;

    @ColumnInfo(name = "stf_short")
    private String stf_short;

    @ColumnInfo(name = "stf_staff_id")
    private String stf_staff_id;

    public RoomPythologyLabResult(String all_code, String all_short, String c_cont_per, String c_cp_email, String c_cp_mob, String c_dis_nam, String c_dis_short, String c_hf_addr, String c_hf_nam, String c_hf_typ, String c_st_nam, String c_st_short, String c_tu_name, String n_dis_id, String n_hf_cd, String n_hf_id, String n_hf_typ_id, String n_oth_sanc_id, String n_pc_sanc_id, String n_pm_sanc_id, String n_st_id, String n_stf_sanc_id, String n_tu_id, String oth_lng, String oth_nam, String oth_staff_id, String pc_code, String pc_lng, String pc_nam, String pc_short, String pc_staff_id, String pm_code, String pm_lng, String pm_nam, String pm_short, String pm_staff_id, String stf_code, String stf_lng, String stf_nam, String stf_short, String stf_staff_id) {
        this.all_code = all_code;
        this.all_short = all_short;
        this.c_cont_per = c_cont_per;
        this.c_cp_email = c_cp_email;
        this.c_cp_mob = c_cp_mob;
        this.c_dis_nam = c_dis_nam;
        this.c_dis_short = c_dis_short;
        this.c_hf_addr = c_hf_addr;
        this.c_hf_nam = c_hf_nam;
        this.c_hf_typ = c_hf_typ;
        this.c_st_nam = c_st_nam;
        this.c_st_short = c_st_short;
        this.c_tu_name = c_tu_name;
        this.n_dis_id = n_dis_id;
        this.n_hf_cd = n_hf_cd;
        this.n_hf_id = n_hf_id;
        this.n_hf_typ_id = n_hf_typ_id;
        this.n_oth_sanc_id = n_oth_sanc_id;
        this.n_pc_sanc_id = n_pc_sanc_id;
        this.n_pm_sanc_id = n_pm_sanc_id;
        this.n_st_id = n_st_id;
        this.n_stf_sanc_id = n_stf_sanc_id;
        this.n_tu_id = n_tu_id;
        this.oth_lng = oth_lng;
        this.oth_nam = oth_nam;
        this.oth_staff_id = oth_staff_id;
        this.pc_code = pc_code;
        this.pc_lng = pc_lng;
        this.pc_nam = pc_nam;
        this.pc_short = pc_short;
        this.pc_staff_id = pc_staff_id;
        this.pm_code = pm_code;
        this.pm_lng = pm_lng;
        this.pm_nam = pm_nam;
        this.pm_short = pm_short;
        this.pm_staff_id = pm_staff_id;
        this.stf_code = stf_code;
        this.stf_lng = stf_lng;
        this.stf_nam = stf_nam;
        this.stf_short = stf_short;
        this.stf_staff_id = stf_staff_id;
    }

    public String getAll_code() {
        return all_code;
    }

    public void setAll_code(String all_code) {
        this.all_code = all_code;
    }

    public String getAll_short() {
        return all_short;
    }

    public void setAll_short(String all_short) {
        this.all_short = all_short;
    }

    public String getC_cont_per() {
        return c_cont_per;
    }

    public void setC_cont_per(String c_cont_per) {
        this.c_cont_per = c_cont_per;
    }

    public String getC_cp_email() {
        return c_cp_email;
    }

    public void setC_cp_email(String c_cp_email) {
        this.c_cp_email = c_cp_email;
    }

    public String getC_cp_mob() {
        return c_cp_mob;
    }

    public void setC_cp_mob(String c_cp_mob) {
        this.c_cp_mob = c_cp_mob;
    }

    public String getC_dis_nam() {
        return c_dis_nam;
    }

    public void setC_dis_nam(String c_dis_nam) {
        this.c_dis_nam = c_dis_nam;
    }

    public String getC_dis_short() {
        return c_dis_short;
    }

    public void setC_dis_short(String c_dis_short) {
        this.c_dis_short = c_dis_short;
    }

    public String getC_hf_addr() {
        return c_hf_addr;
    }

    public void setC_hf_addr(String c_hf_addr) {
        this.c_hf_addr = c_hf_addr;
    }

    public String getC_hf_nam() {
        return c_hf_nam;
    }

    public void setC_hf_nam(String c_hf_nam) {
        this.c_hf_nam = c_hf_nam;
    }

    public String getC_hf_typ() {
        return c_hf_typ;
    }

    public void setC_hf_typ(String c_hf_typ) {
        this.c_hf_typ = c_hf_typ;
    }

    public String getC_st_nam() {
        return c_st_nam;
    }

    public void setC_st_nam(String c_st_nam) {
        this.c_st_nam = c_st_nam;
    }

    public String getC_st_short() {
        return c_st_short;
    }

    public void setC_st_short(String c_st_short) {
        this.c_st_short = c_st_short;
    }

    public String getC_tu_name() {
        return c_tu_name;
    }

    public void setC_tu_name(String c_tu_name) {
        this.c_tu_name = c_tu_name;
    }

    public String getN_dis_id() {
        return n_dis_id;
    }

    public void setN_dis_id(String n_dis_id) {
        this.n_dis_id = n_dis_id;
    }

    public String getN_hf_cd() {
        return n_hf_cd;
    }

    public void setN_hf_cd(String n_hf_cd) {
        this.n_hf_cd = n_hf_cd;
    }

    public String getN_hf_id() {
        return n_hf_id;
    }

    public void setN_hf_id(String n_hf_id) {
        this.n_hf_id = n_hf_id;
    }

    public String getN_hf_typ_id() {
        return n_hf_typ_id;
    }

    public void setN_hf_typ_id(String n_hf_typ_id) {
        this.n_hf_typ_id = n_hf_typ_id;
    }

    public String getN_oth_sanc_id() {
        return n_oth_sanc_id;
    }

    public void setN_oth_sanc_id(String n_oth_sanc_id) {
        this.n_oth_sanc_id = n_oth_sanc_id;
    }

    public String getN_pc_sanc_id() {
        return n_pc_sanc_id;
    }

    public void setN_pc_sanc_id(String n_pc_sanc_id) {
        this.n_pc_sanc_id = n_pc_sanc_id;
    }

    public String getN_pm_sanc_id() {
        return n_pm_sanc_id;
    }

    public void setN_pm_sanc_id(String n_pm_sanc_id) {
        this.n_pm_sanc_id = n_pm_sanc_id;
    }

    public String getN_st_id() {
        return n_st_id;
    }

    public void setN_st_id(String n_st_id) {
        this.n_st_id = n_st_id;
    }

    public String getN_stf_sanc_id() {
        return n_stf_sanc_id;
    }

    public void setN_stf_sanc_id(String n_stf_sanc_id) {
        this.n_stf_sanc_id = n_stf_sanc_id;
    }

    public String getN_tu_id() {
        return n_tu_id;
    }

    public void setN_tu_id(String n_tu_id) {
        this.n_tu_id = n_tu_id;
    }

    public String getOth_lng() {
        return oth_lng;
    }

    public void setOth_lng(String oth_lng) {
        this.oth_lng = oth_lng;
    }

    public String getOth_nam() {
        return oth_nam;
    }

    public void setOth_nam(String oth_nam) {
        this.oth_nam = oth_nam;
    }

    public String getOth_staff_id() {
        return oth_staff_id;
    }

    public void setOth_staff_id(String oth_staff_id) {
        this.oth_staff_id = oth_staff_id;
    }

    public String getPc_code() {
        return pc_code;
    }

    public void setPc_code(String pc_code) {
        this.pc_code = pc_code;
    }

    public String getPc_lng() {
        return pc_lng;
    }

    public void setPc_lng(String pc_lng) {
        this.pc_lng = pc_lng;
    }

    public String getPc_nam() {
        return pc_nam;
    }

    public void setPc_nam(String pc_nam) {
        this.pc_nam = pc_nam;
    }

    public String getPc_short() {
        return pc_short;
    }

    public void setPc_short(String pc_short) {
        this.pc_short = pc_short;
    }

    public String getPc_staff_id() {
        return pc_staff_id;
    }

    public void setPc_staff_id(String pc_staff_id) {
        this.pc_staff_id = pc_staff_id;
    }

    public String getPm_code() {
        return pm_code;
    }

    public void setPm_code(String pm_code) {
        this.pm_code = pm_code;
    }

    public String getPm_lng() {
        return pm_lng;
    }

    public void setPm_lng(String pm_lng) {
        this.pm_lng = pm_lng;
    }

    public String getPm_nam() {
        return pm_nam;
    }

    public void setPm_nam(String pm_nam) {
        this.pm_nam = pm_nam;
    }

    public String getPm_short() {
        return pm_short;
    }

    public void setPm_short(String pm_short) {
        this.pm_short = pm_short;
    }

    public String getPm_staff_id() {
        return pm_staff_id;
    }

    public void setPm_staff_id(String pm_staff_id) {
        this.pm_staff_id = pm_staff_id;
    }

    public String getStf_code() {
        return stf_code;
    }

    public void setStf_code(String stf_code) {
        this.stf_code = stf_code;
    }

    public String getStf_lng() {
        return stf_lng;
    }

    public void setStf_lng(String stf_lng) {
        this.stf_lng = stf_lng;
    }

    public String getStf_nam() {
        return stf_nam;
    }

    public void setStf_nam(String stf_nam) {
        this.stf_nam = stf_nam;
    }

    public String getStf_short() {
        return stf_short;
    }

    public void setStf_short(String stf_short) {
        this.stf_short = stf_short;
    }

    public String getStf_staff_id() {
        return stf_staff_id;
    }

    public void setStf_staff_id(String stf_staff_id) {
        this.stf_staff_id = stf_staff_id;
    }

    public Integer getIds() {
        return ids;
    }
}
