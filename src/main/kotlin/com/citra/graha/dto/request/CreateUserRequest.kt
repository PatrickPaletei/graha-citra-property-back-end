package com.citra.graha.dto.request

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val roleId: Int
)
