package com.citra.graha.service

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.entity.MstProduct
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface ProductPhotoService {
    fun addPhotoOrVideo(photoOrVideo: List<MultipartFile>, idPhoto: MstProduct): ResponseEntity<BaseResponse<Any>>
    fun getPhotoOrVideoByProductId(idProduct: MstProduct):ResponseEntity<BaseResponse<List<PhotoResponse>>>
    fun deletePhoto(idProductPhoto:Int):ResponseEntity<BaseResponse<Any>>
    fun loadPhoto(fileName:String):ResponseEntity<Any>
}