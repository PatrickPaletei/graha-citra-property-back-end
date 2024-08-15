package com.citra.graha.util.swaggerschema

import com.citra.graha.entity.MstWorkExperience

data class BaseResponseWithListMstWorkExperience(
    val status: String,
    val message: String,
    val data: List<MstWorkExperience>
)
