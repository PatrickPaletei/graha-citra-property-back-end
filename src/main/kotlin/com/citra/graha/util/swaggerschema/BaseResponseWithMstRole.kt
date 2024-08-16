package com.citra.graha.util.swaggerschema

import com.citra.graha.entity.MstRole

data class BaseResponseWithMstRole(
    val status: String,
    val message: String,
    val data: MstRole
)
