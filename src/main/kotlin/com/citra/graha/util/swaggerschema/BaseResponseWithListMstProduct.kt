package com.citra.graha.util.swaggerschema

import com.citra.graha.entity.MstProduct

data class BaseResponseWithListMstProduct(
    val status: String,
    val message: String,
    val data: List<MstProduct>
)
