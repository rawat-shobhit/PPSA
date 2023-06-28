package com.smit.ppsa.sampleCollectionVisitFolder

import com.google.gson.annotations.SerializedName

data class SampleCollectionVisitResponseModel(
    @SerializedName("status"    ) var status   : Boolean?             = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("user_data" ) var userData : ArrayList<SampleCollectionVisitList> = arrayListOf()

)
