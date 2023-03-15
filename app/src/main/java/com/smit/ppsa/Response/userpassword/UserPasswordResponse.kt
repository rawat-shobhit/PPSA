package com.smit.ppsa.Response.userpassword

data class UserPasswordResponse(
    val message: String?,
    val status: String?,
    val user_data: List<UserData>?
)