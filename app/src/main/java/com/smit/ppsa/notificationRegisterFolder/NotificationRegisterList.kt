package com.smit.ppsa.notificationRegisterFolder

import com.google.gson.annotations.SerializedName

data class NotificationRegisterList(
    @SerializedName("c_hf_nam"  ) var cHfNam  : String? = null,
    @SerializedName("n_nksh_id" ) var nNkshId : String? = null,
    @SerializedName("c_pat_nam" ) var cPatNam : String? = null,
    @SerializedName("notf"      ) var notf    : String? = null,
    @SerializedName("bnk"       ) var bnk     : String? = null,
    @SerializedName("udst"      ) var udst    : String? = null
)
