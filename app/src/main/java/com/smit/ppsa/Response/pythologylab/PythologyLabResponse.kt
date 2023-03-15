package com.smit.ppsa.Response.pythologylab

data class PythologyLabResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)