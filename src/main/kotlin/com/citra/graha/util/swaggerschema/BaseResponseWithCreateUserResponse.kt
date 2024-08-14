package com.citra.graha.util.swaggerschema

import com.citra.graha.dto.response.CreateUserResponse

data class BaseResponseWithCreateUserResponse(
    val status: String,
    val message: String,
    val data: CreateUserResponse
)