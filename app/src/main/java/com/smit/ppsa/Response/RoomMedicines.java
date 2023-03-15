package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "roomMedicines")
public class RoomMedicines implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "c_val")
    private String c_val;

    private String UOM="";

    private int uomPos = 0;

    private int qty=1;

    public void setIds(Integer ids) {
        this.ids = ids;
    }

    public int getUomPos() {
        return uomPos;
    }

    public void setUomPos(int uomPos) {
        this.uomPos = uomPos;
    }

    private Boolean checked = false;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public RoomMedicines(String id, String c_val) {
        this.id = id;
        this.c_val = c_val;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
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
