package com.smit.ppsa.providerStatusFolder

import com.google.gson.annotations.SerializedName

data class ProviderVisitList(

    @SerializedName("c_hf_nam"  ) var cHfNam  : String? = null,
    @SerializedName("c_doc_nam" ) var cDocNam : String? = null,
    @SerializedName("c_val"     ) var cVal    : String? = null,
    @SerializedName("prd"       ) var prd     : String? = null,
    @SerializedName("n_user_id" ) var nUserId : String? = null
)
