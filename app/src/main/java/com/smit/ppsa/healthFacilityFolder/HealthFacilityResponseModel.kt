package com.smit.ppsa.healthFacilityFolder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.smit.ppsa.Response.RegisterParentData

data class HealthFacilityResponseModel(
    @SerializedName("status"    ) var status   : Boolean?             = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("user_data" ) var userData : ArrayList<HealthFacilityList> = arrayListOf()
) {

}