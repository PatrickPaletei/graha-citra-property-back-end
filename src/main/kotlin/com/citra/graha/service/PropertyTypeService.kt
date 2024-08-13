package com.citra.graha.service

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PropertyTypeResponse
import com.citra.graha.entity.MstPropertyType
import org.springframework.http.ResponseEntity

interface PropertyTypeService {
    fun addPropertyType(propertyType: String): ResponseEntity<BaseResponse<MstPropertyType>>
    fun getPropertyTypes(): ResponseEntity<BaseResponse<List<PropertyTypeResponse>>>
    fun deletePropertyType(propertyTypeId: Int): ResponseEntity<BaseResponse<Any>>
    fun updatePropertyType(mstPropertyType: MstPropertyType,propertyTypeId: Int, propertyTypeName: String): ResponseEntity<BaseResponse<PropertyTypeResponse>>
}