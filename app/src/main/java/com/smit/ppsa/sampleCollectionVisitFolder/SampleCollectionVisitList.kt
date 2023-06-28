package com.smit.ppsa.sampleCollectionVisitFolder

import com.google.gson.annotations.SerializedName

data class SampleCollectionVisitList(
    @SerializedName("n_nksh_id" ) var nNkshId : String? = null,
    @SerializedName("c_pat_nam" ) var cPatNam : String? = null,
    @SerializedName("c_val"     ) var cVal    : String? = null,
    @SerializedName("rpt_coll"  ) var rptColl : String? = null,
    @SerializedName("prd"       ) var prd     : String? = null,
    @SerializedName("n_user_id" ) var nUserId : String? = null
)
