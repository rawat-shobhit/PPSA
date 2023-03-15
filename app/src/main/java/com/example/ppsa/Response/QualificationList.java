package com.example.ppsa.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QualificationList {

    @SerializedName("c_qual")
    @Expose
    private String cQual="";
    @SerializedName("id")
    @Expose
    private String id="";
    @SerializedName("n_hf_typ_id")
    @Expose
    private String nHfTypId;
    @SerializedName("c_qualf")
    @Expose
    private String cQualf="";

    @SerializedName("c_hf_typ")
    @Expose
    private String c_hf_typ="";

    @SerializedName("c_soe")
    private String c_soe="";

    @SerializedName("c_typ")
    private String c_typ="";

    public String getC_typ() {
        return c_typ;
    }

    public void setC_typ(String c_typ) {
        this.c_typ = c_typ;
    }

    public String getC_soe() {
        return c_soe;
    }

    public void setC_soe(String c_soe) {
        this.c_soe = c_soe;
    }

    public String getC_hf_typ() {
        return c_hf_typ;
    }

    public void setC_hf_typ(String c_hf_typ) {
        this.c_hf_typ = c_hf_typ;
    }

    public String getcQualf() {
        return cQualf;
    }

    public void setcQualf(String cQualf) {
        this.cQualf = cQualf;
    }



    public String getcQual() {
        return cQual;
    }

    public void setcQual(String cQual) {
        this.cQual = cQual;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getnHfTypId() {
        return nHfTypId;
    }

    public void setnHfTypId(String nHfTypId) {
        this.nHfTypId = nHfTypId;
    }

}
