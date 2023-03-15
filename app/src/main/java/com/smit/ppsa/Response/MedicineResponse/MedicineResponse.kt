package com.smit.ppsa.Response.MedicineResponse

data class MedicineResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)