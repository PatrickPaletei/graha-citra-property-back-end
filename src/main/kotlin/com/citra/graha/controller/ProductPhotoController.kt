package com.citra.graha.controller

import com.citra.graha.dto.request.AddPhotoRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.ProductPhoto
import com.citra.graha.repository.MstProductRepository
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.service.ProductPhotoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/product")
class ProductPhotoController(
    val productPhotoService: ProductPhotoService,
    val productPhotoRepository: ProductPhotoRepository,
    val mstProductRepository: MstProductRepository
) {
    @PostMapping("/product_photo")
    fun createProductPhoto(@RequestBody productPhoto: AddPhotoRequest): ResponseEntity<BaseResponse<Any>> {

        val existProductId = mstProductRepository.findByProductId(productPhoto.productId)
        if (existProductId.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Product's id not found",
                    data = null
                )
            )
        }

        return productPhotoService.addPhoto(productPhoto,existProductId.get())

    }
//    @GetMapping("/{productId}")
//    fun getProductPhoto(@PathVariable productId: Int): ResponseEntity<BaseResponse<Any>> {
//        return productPhotoService.getProductId(Integer.valueOf(productId))
//    }
}