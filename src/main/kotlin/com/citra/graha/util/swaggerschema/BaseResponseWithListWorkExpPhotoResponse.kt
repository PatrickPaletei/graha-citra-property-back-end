package com.citra.graha.util.swaggerschema

import com.citra.graha.dto.response.WorkExpPhotoResponse

data class BaseResponseWithListWorkExpPhotoResponse(
    val status: String,
    val message: String,
    val data: List<WorkExpPhotoResponse>
)