package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomPrevVisitsData")
public class RoomPrevVisitsData {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;

    @ColumnInfo(name = "d_visit")
    public String dVisit;

    @ColumnInfo(name = "c_val")
    public String cVal;

    @ColumnInfo(name = "n_hf_id")
    public String nHfId;


    public RoomPrevVisitsData(String dVisit, String cVal, String nHfId) {
        this.dVisit = dVisit;
        this.cVal = cVal;
        this.nHfId = nHfId;
    }

    public String getdVisit() {
        return dVisit;
    }

    public void setdVisit(String dVisit) {
        this.dVisit = dVisit;
    }

    public String getcVal() {
        return cVal;
    }

    public void setcVal(String cVal) {
        this.cVal = cVal;
    }

    public String getnHfId() {
        return nHfId;
    }

    public void setnHfId(String nHfId) {
        this.nHfId = nHfId;
    }

    public Integer getIds() {
        return ids;
    }
}
