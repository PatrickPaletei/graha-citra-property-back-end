package com.citra.graha.service

import com.citra.graha.dto.request.AddProductRequest
import com.citra.graha.dto.request.UpdateProductRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.MstPropertyType
import com.citra.graha.entity.MstStatus
import org.springframework.http.ResponseEntity

interface ProductService {
    fun addProduct(product: AddProductRequest, propertyType: MstPropertyType, status: MstStatus): ResponseEntity<BaseResponse<MstProduct>>
    fun deleteProduct(product: MstProduct): ResponseEntity<BaseResponse<Any>>
    fun getAll(): ResponseEntity<BaseResponse<List<MstProduct>>>
    fun getById(id: Int): ResponseEntity<BaseResponse<MstProduct>>
    fun getByPropertyTypeName(propertyType: String): ResponseEntity<BaseResponse<List<MstProduct>>>
    fun getByPropertyTypeId(propertyType: Int): ResponseEntity<BaseResponse<List<MstProduct>>>
    fun getByStatusName(statusName: String): ResponseEntity<BaseResponse<List<MstProduct>>>
    fun getByStatusId(statusId: Int): ResponseEntity<BaseResponse<List<MstProduct>>>
    fun updateProduct(existProduct: MstProduct, product: UpdateProductRequest, propertyType: MstPropertyType?, status: MstStatus?): ResponseEntity<BaseResponse<MstProduct>>
}