package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name="lst_vst")
    private final String lst_vst;

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Integer getIds() {
        return ids;
    }

    public String getlst_vst() {
        return lst_vst;
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

    public RoomDoctorsList(String docname, String qualf, String qual, String mob, String hf_id, String idd, String lst_vst) {
        this.docname = docname;
        this.qualf = qualf;
        this.qual = qual;
        this.mob = mob;
        this.hf_id = hf_id;
        this.idd = idd;
        this.lst_vst = lst_vst;
    }

    public String getLst_vst() {
        return lst_vst;
    }
}
