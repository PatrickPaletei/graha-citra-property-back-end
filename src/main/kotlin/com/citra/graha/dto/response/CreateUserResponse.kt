package com.citra.graha.dto.response

import com.citra.graha.entity.MstRole

data class CreateUserResponse(
    val userId: Int,
    val username: String,
    val status: String,
    val role: MstRole
)
