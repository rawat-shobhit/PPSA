package com.smit.ppsa.notificationRegisterFolder

import com.google.gson.annotations.SerializedName

data class NotificationRegisterResponse(

    @SerializedName("status"    ) var status   : Boolean?             = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("user_data" ) var userData : ArrayList<NotificationRegisterList> = arrayListOf()
)
