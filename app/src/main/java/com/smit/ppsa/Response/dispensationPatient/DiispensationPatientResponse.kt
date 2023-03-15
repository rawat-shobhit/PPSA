package com.smit.ppsa.Response.dispensationPatient

data class DiispensationPatientResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)