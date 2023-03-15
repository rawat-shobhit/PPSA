package com.example.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormTwoData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("c_test_reas")
    @Expose
    private String cTestReas;

    @SerializedName("c_typ_specm")
    @Expose
    private String cTypSpecm;


    @SerializedName("c_smpl_ext")
    @Expose
    private String cSmplExt;

    @SerializedName("c_sputm_typ")
    @Expose
    private String cSputmTyp;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcTestReas() {
        return cTestReas;
    }

    public void setcTestReas(String cTestReas) {
        this.cTestReas = cTestReas;
    }

    public String getcTypSpecm() {
        return cTypSpecm;
    }

    public void setcTypSpecm(String cTypSpecm) {
        this.cTypSpecm = cTypSpecm;
    }


    public String getcSmplExt() {
        return cSmplExt;
    }

    public void setcSmplExt(String cSmplExt) {
        this.cSmplExt = cSmplExt;
    }

    public String getcSputmTyp() {
        return cSputmTyp;
    }

    public void setcSputmTyp(String cSputmTyp) {
        this.cSputmTyp = cSputmTyp;
    }

}
