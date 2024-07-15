package com.citra.graha.service.impl

import com.citra.graha.dto.request.AddPhotoRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.ProductPhoto
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.service.ProductPhotoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ProductPhotoImpl(
    val productPhotoRepository: ProductPhotoRepository
): ProductPhotoService {
    override fun addPhoto(photo: AddPhotoRequest, idPhoto: MstProduct): ResponseEntity<BaseResponse<Any>> {
        val photo = ProductPhoto(
            productId = idPhoto,
            base64Photo = photo.path
        )
        productPhotoRepository.save(photo)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Photo added successfull"
            )
        )
    }

//    override fun getProductId(productId: Int): ResponseEntity<BaseResponse<Any>> {
//
//    }

}

