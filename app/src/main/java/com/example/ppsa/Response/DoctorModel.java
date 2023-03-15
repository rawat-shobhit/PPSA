package com.example.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DoctorModel {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "n_hf_id")
    private String n_hf_id;

    @ColumnInfo(name = "c_doc_nam")
    private String c_doc_nam;

    @ColumnInfo(name = "n_qual_id")
    private String n_qual_id;

    @ColumnInfo(name = "n_spec_id")
    private String n_spec_id;

    @ColumnInfo(name = "c_mob")
    private String c_mob;

    public Integer getId() {
        return id;
    }

    public DoctorModel(String n_hf_id, String c_doc_nam, String n_qual_id, String n_spec_id, String c_mob) {
        this.n_hf_id = n_hf_id;
        this.c_doc_nam = c_doc_nam;
        this.n_qual_id = n_qual_id;
        this.n_spec_id = n_spec_id;
        this.c_mob = c_mob;
    }

    public String getC_doc_nam() {
        return c_doc_nam;
    }

    public void setC_doc_nam(String c_doc_nam) {
        this.c_doc_nam = c_doc_nam;
    }

    public String getC_mob() {
        return c_mob;
    }

    public void setC_mob(String c_mob) {
        this.c_mob = c_mob;
    }

    public String getN_hf_id() {
        return n_hf_id;
    }

    public void setN_hf_id(String n_hf_id) {
        this.n_hf_id = n_hf_id;
    }

    public String getN_qual_id() {
        return n_qual_id;
    }

    public void setN_qual_id(String n_qual_id) {
        this.n_qual_id = n_qual_id;
    }

    public String getN_spec_id() {
        return n_spec_id;
    }

    public void setN_spec_id(String n_spec_id) {
        this.n_spec_id = n_spec_id;
    }
}
