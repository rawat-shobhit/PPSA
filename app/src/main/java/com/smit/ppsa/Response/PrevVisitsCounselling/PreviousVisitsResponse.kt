package com.smit.ppsa.Response.PrevVisitsCounselling

data class PreviousVisitsResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)