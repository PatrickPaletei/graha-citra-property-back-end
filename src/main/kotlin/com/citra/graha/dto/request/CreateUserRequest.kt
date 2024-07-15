package com.citra.graha.dto.request

data class CreateUserRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val status: String? = null,
    val roleId: Int? = null
)
