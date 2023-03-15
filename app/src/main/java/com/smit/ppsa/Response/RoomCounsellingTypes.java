package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomCounsellingTypes")
public class RoomCounsellingTypes {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "c_val")
    private String c_val;


    public RoomCounsellingTypes(String id, String c_val) {
        this.id = id;
        this.c_val = c_val;
    }


    public String getC_val() {
        return c_val;
    }

    public void setC_val(String c_val) {
        this.c_val = c_val;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIds() {
        return ids;
    }
}
