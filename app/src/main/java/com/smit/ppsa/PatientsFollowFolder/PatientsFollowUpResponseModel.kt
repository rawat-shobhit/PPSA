package com.smit.ppsa.PatientsFollowFolder

import com.google.gson.annotations.SerializedName

data class PatientsFollowUpResponseModel(
    @SerializedName("status"    ) var status   : Boolean?             = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("user_data" ) var userData : ArrayList<PatientFollowUpList> = arrayListOf()
)
