package com.citra.graha.dto.request

data class CreateUserRequest(
    val id: Int? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val status: String? = "01",
    val roleId: Int? = null
)
