package com.citra.graha.util.swaggerschema

import com.citra.graha.entity.MstStatus

data class BaseResponseWithListMstStatus(
    val status: String,
    val message: String,
    val data: List<MstStatus>
)
