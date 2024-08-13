package com.citra.graha.util.swaggerschema

import com.citra.graha.dto.response.PropertyTypeResponse

data class BaseResponseWithListPropertyTypeResponse(
    val status: String,
    val message: String,
    val data: List<PropertyTypeResponse>
)
