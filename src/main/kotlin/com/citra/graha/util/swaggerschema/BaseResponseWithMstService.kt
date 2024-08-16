package com.citra.graha.util.swaggerschema

import com.citra.graha.entity.MstService

data class BaseResponseWithMstService(
    val status: String,
    val message: String,
    val data: MstService
)
