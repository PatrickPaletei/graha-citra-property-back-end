package com.citra.graha.service

import com.citra.graha.dto.request.AddPhotoRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.ProductPhoto
import org.springframework.http.ResponseEntity

interface ProductPhotoService {
    fun addPhoto(photo: AddPhotoRequest, idPhoto:MstProduct): ResponseEntity<BaseResponse<Any>>
//    fun getProductId(productId: Int): ResponseEntity<BaseResponse<Any>>
}