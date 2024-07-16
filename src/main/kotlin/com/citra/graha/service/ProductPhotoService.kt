package com.citra.graha.service

import com.citra.graha.dto.request.AddPhotoRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.entity.MstProduct
import org.springframework.http.ResponseEntity

interface ProductPhotoService {
    fun addPhoto(photo: AddPhotoRequest, idPhoto:MstProduct): ResponseEntity<BaseResponse<Any>>
    fun getPhotoByProductId(idProduct: MstProduct):ResponseEntity<BaseResponse<List<PhotoResponse>>>
    fun deletePhoto(idProductPhoto:Int):ResponseEntity<BaseResponse<Any>>
}