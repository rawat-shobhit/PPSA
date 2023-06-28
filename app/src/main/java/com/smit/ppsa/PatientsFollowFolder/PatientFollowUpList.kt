package com.smit.ppsa.PatientsFollowFolder

import com.google.gson.annotations.SerializedName

data class PatientFollowUpList (
    @SerializedName("c_hf_nam"  ) var cHfNam  : String? = null,
    @SerializedName("n_nksh_id" ) var nNkshId : String? = null,
    @SerializedName("c_pat_nam" ) var cPatNam : String? = null,
    @SerializedName("c_val"     ) var cVal    : String? = null,
    @SerializedName("prd") var prd     : String? = null,
    @SerializedName("n_user_id" ) var nUserId : String? = null
){
}