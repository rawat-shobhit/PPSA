package com.example.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "roomDoctorsList", indices = @Index(value = {"idd"}, unique = true))
public class RoomDoctorsList {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "docname")
    private final String docname;
    @ColumnInfo(name = "qualf")
    private final String qualf;
    @ColumnInfo(name = "qual")
    private final String qual;
    @ColumnInfo(name = "mob")
    private final String mob;
    @ColumnInfo(name = "hf_id")
    private final String hf_id;
    @ColumnInfo(name = "idd")
    private final String idd;

    public Integer getIds() {
        return ids;
    }

    public String getDocname() {
        return docname;
    }

    public String getQualf() {
        return qualf;
    }

    public String getQual() {
        return qual;
    }

    public String getMob() {
        return mob;
    }

    public String getHf_id() {
        return hf_id;
    }

    public String getIdd() {
        return idd;
    }

    public RoomDoctorsList(String docname, String qualf, String qual, String mob, String hf_id, String idd) {
        this.docname = docname;
        this.qualf = qualf;
        this.qual = qual;
        this.mob = mob;
        this.hf_id = hf_id;
        this.idd = idd;
    }
}
