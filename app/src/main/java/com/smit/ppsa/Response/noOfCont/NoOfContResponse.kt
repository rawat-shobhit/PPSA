package com.smit.ppsa.Response.noOfCont

data class NoOfContResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)