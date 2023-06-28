package com.smit.ppsa.providerStatusFolder

import com.google.gson.annotations.SerializedName

data class ProviderVistResponseModel(
    @SerializedName("status"    ) var status   : Boolean?             = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("user_data" ) var userData : ArrayList<ProviderVisitList> = arrayListOf()
)
