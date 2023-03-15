package com.smit.ppsa.Response.CollectedBy

data class CollectedByResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)