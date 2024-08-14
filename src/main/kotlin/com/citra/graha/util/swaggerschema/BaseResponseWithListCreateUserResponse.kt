package com.citra.graha.util.swaggerschema

import com.citra.graha.dto.response.CreateUserResponse

data class BaseResponseWithListCreateUserResponse(
    val status: String,
    val message: String,
    val data: List<CreateUserResponse>
)