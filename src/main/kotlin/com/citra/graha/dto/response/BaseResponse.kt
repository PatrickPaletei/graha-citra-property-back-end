package com.citra.graha.dto.response

data class BaseResponse<T>(
    val status: String? = "T",
    val message: String? = "",
    val data: T? = null
)
