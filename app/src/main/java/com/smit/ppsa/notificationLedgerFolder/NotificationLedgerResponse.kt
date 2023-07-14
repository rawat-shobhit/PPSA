package com.smit.ppsa.notificationLedgerFolder

import com.google.gson.annotations.SerializedName

data class NotificationLedgerResponse(

    @SerializedName("status"    ) var status   : Boolean?             = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("user_data" ) var userData : ArrayList<NotificationLedgerList> = arrayListOf()
)
