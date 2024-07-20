package com.citra.graha.dto.response

import org.springframework.http.HttpStatus

data class DeleteResponse(
    val statusCode: HttpStatus,
    val statusString: String,
    val message: String
)
