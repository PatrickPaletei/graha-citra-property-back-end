package com.citra.graha.service.impl

import com.citra.graha.dto.request.AddPhotoRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.ProductPhoto
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.service.ProductPhotoService
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ProductPhotoImpl(
    val productPhotoRepository: ProductPhotoRepository,
): ProductPhotoService {
    override fun addPhoto(photo: AddPhotoRequest, idPhoto: MstProduct): ResponseEntity<BaseResponse<Any>> {
        photo.path.forEach { base64String ->
            val productPhoto = ProductPhoto(
                productId = idPhoto,
                base64Photo = base64String
            )
            productPhotoRepository.save(productPhoto)
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Photos added successfully"
            )
        )
    }

    override fun getPhotoByProductId(idProduct: MstProduct): ResponseEntity<BaseResponse<List<PhotoResponse>>> {
        val photo = productPhotoRepository.findByProductId(idProduct)
        if (photo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse(
                status = "T",
                message = "Photo not found"
            ))
        }
        val listPhoto = mutableListOf<PhotoResponse>()
        photo.forEach {
            listPhoto.add(
                PhotoResponse(
                    productPhotoId = it.productPhotoId!!,
                    base64photo = it.base64Photo
                )
            )
        }
        return ResponseEntity.ok().body(
            BaseResponse(
                status = "T",
                message = "Photos found",
                data = listPhoto
            )
        )
    }

    override fun deletePhoto(idProductPhoto: Int): ResponseEntity<BaseResponse<Any>> {
        val photo = productPhotoRepository.findById(idProductPhoto)
        productPhotoRepository.delete(photo.get())
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Photo deleted",
                data = null
            )
        )
    }


}

