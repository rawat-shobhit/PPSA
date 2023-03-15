package com.smit.ppsa.Response.CounsellingResponse

data class CounsellingResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)