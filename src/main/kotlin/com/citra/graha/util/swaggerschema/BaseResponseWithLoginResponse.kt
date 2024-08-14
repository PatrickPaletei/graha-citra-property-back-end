package com.citra.graha.util.swaggerschema

import com.citra.graha.dto.response.LoginResponse

data class BaseResponseWithLoginResponse(
    val status: String,
    val message: String,
    val data: LoginResponse
)
