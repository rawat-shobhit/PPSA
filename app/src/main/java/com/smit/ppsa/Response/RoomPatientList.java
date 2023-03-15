package com.smit.ppsa.Response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "roomPatientList", indices = @Index(value = {"id"}, unique = true))
public class RoomPatientList {
    @PrimaryKey(autoGenerate = true)
    public Integer ids;
    @ColumnInfo(name = "d_reg_dat")
    public final String dRegDat;
    @ColumnInfo(name = "n_nksh_id")
    public String nNkshId;
    @ColumnInfo(name = "c_pat_nam")
    public String cPatNam;
    @ColumnInfo(name = "n_age")
    public String nAge;
    @ColumnInfo(name = "c_typ")
    public String cTyp;
    @ColumnInfo(name = "n_wght")
    public String nWght;
    @ColumnInfo(name = "n_hght")
    public String nHght;
    @ColumnInfo(name = "c_add")
    public String cAdd;
    @ColumnInfo(name = "c_taluka")
    public String cTaluka;
    @ColumnInfo(name = "c_town")
    public String cTown;
    @ColumnInfo(name = "c_ward")
    public String cWard;
    @ColumnInfo(name = "c_lnd_mrk")
    public String cLndMrk;
    @ColumnInfo(name = "n_pin")
    public String nPin;
    @ColumnInfo(name = "c_doc_nam")
    public String cDocNam;
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "n_st_id")
    public String nStId;
    @ColumnInfo(name = "n_dis_id")
    public String nDisId;
    @ColumnInfo(name = "n_tu_id")
    public String nTuId;
    @ColumnInfo(name = "n_hf_id")
    public String nHfId;
    @ColumnInfo(name = "n_doc_id")
    public String nDocId;
    @ColumnInfo(name = "n_user_id")
    public String nUserId;

    @ColumnInfo(name = "no_days")
    public String no_days;
    @ColumnInfo(name = "LST_DT")
    public String LST_DT;

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public RoomPatientList(String dRegDat, String nNkshId, String cPatNam, String nAge, String cTyp, String nWght, String nHght, String cAdd, String cTaluka, String cTown, String cWard, String cLndMrk, String nPin, String cDocNam, String id, String nStId, String nDisId, String nTuId, String nHfId, String nDocId, String nUserId, String no_days, String LST_DT ) {
        this.dRegDat = dRegDat;
        this.nNkshId = nNkshId;
        this.cPatNam = cPatNam;
        this.nAge = nAge;
        this.cTyp = cTyp;
        this.nWght = nWght;
        this.nHght = nHght;
        this.cAdd = cAdd;
        this.cTaluka = cTaluka;
        this.cTown = cTown;
        this.cWard = cWard;
        this.cLndMrk = cLndMrk;
        this.nPin = nPin;
        this.cDocNam = cDocNam;
        this.id = id;
        this.nStId = nStId;
        this.nDisId = nDisId;
        this.nTuId = nTuId;
        this.nHfId = nHfId;
        this.nDocId = nDocId;
        this.nUserId = nUserId;
        this.no_days = no_days;
        this.LST_DT = LST_DT;
    }

    public Integer getIds() {
        return ids;
    }

    public String getdRegDat() {
        return dRegDat;
    }

    public String getnNkshId() {
        return nNkshId;
    }

    public String getcPatNam() {
        return cPatNam;
    }

    public String getnAge() {
        return nAge;
    }

    public String getcTyp() {
        return cTyp;
    }

    public String getnWght() {
        return nWght;
    }

    public String getnHght() {
        return nHght;
    }

    public String getcAdd() {
        return cAdd;
    }

    public String getcTaluka() {
        return cTaluka;
    }

    public String getcTown() {
        return cTown;
    }

    public String getcWard() {
        return cWard;
    }

    public String getcLndMrk() {
        return cLndMrk;
    }

    public String getnPin() {
        return nPin;
    }

    public String getcDocNam() {
        return cDocNam;
    }

    public String getId() {
        return id;
    }

    public String getnStId() {
        return nStId;
    }

    public String getnDisId() {
        return nDisId;
    }

    public String getnTuId() {
        return nTuId;
    }

    public String getnHfId() {
        return nHfId;
    }

    public String getnDocId() {
        return nDocId;
    }

    public String getnUserId() {
        return nUserId;
    }

    public String getNo_days() {
        return no_days;
    }

    public void setNo_days(String no_days) {
        this.no_days = no_days;
    }

    public String getLST_DT() {
        return LST_DT;
    }

    public void setLST_DT(String LST_DT) {
        this.LST_DT = LST_DT;
    }
}
