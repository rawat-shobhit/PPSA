package com.smit.ppsa.Response.testreport

data class TestreportResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)