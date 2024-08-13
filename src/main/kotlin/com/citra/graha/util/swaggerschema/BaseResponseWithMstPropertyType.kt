package com.citra.graha.util.swaggerschema

import com.citra.graha.entity.MstPropertyType

data class BaseResponseWithMstPropertyType(
    val status: String,
    val message: String,
    val data: MstPropertyType
)
