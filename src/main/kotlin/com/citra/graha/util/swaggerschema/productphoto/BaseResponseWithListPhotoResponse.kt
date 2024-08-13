package com.citra.graha.util.swaggerschema.productphoto

import com.citra.graha.dto.response.PhotoResponse

data class BaseResponseWithListPhotoResponse (
    val status: String,
    val message: String,
    val data: List<PhotoResponse>
)