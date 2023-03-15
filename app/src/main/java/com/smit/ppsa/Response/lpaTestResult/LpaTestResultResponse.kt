package com.smit.ppsa.Response.lpaTestResult

data class LpaTestResultResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)