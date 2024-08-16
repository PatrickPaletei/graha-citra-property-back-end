package com.citra.graha.util.swaggerschema

import com.citra.graha.entity.MstStatus

data class BaseResponseWithMstStatus(
    val status: String,
    val message: String,
    val data: MstStatus
)
