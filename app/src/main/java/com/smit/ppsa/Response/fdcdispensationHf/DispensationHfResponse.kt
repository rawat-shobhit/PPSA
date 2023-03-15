package com.smit.ppsa.Response.fdcdispensationHf

data class DispensationHfResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)