package com.citra.graha.util.swaggerschema

import com.citra.graha.entity.MstRole

data class BaseResponseWithListMstRole(
    val status: String,
    val message: String,
    val data: List<MstRole>
)
