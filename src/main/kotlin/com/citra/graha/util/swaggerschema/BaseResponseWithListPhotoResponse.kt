package com.citra.graha.util.swaggerschema

import com.citra.graha.dto.response.PhotoResponse

data class BaseResponseWithListPhotoResponse (
    val status: String,
    val message: String,
    val data: List<PhotoResponse>
)