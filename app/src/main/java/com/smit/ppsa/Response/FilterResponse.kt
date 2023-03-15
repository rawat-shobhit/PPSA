package com.smit.ppsa.Response

data class FilterResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserDataX>?
)