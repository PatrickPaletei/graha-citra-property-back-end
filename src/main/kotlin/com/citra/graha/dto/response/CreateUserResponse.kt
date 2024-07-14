package com.citra.graha.dto.response

import com.citra.graha.entity.MstRole

data class CreateUserResponse(
    val username: String,
    val role: MstRole
)
