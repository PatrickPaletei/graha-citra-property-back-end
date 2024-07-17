package com.citra.graha.dto.response

import com.citra.graha.entity.MstRole

data class LoginResponse(
    val userId: Int,
    val username: String,
    val status: String,
    val token: String,
    val role: MstRole
)
