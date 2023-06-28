package com.smit.ppsa.dailyVisitOutputFolder

import com.google.gson.annotations.SerializedName

data class DailyVisitResponseModel(
    @SerializedName("status"    ) var status   : Boolean?             = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("user_data" ) var userData : ArrayList<DailyVisitList> = arrayListOf()
)
