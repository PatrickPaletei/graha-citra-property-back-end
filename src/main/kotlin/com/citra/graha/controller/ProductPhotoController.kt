package com.citra.graha.controller

import com.citra.graha.dto.request.AddPhotoRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.repository.MstProductRepository
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.service.ProductPhotoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
class ProductPhotoController(
    val productPhotoService: ProductPhotoService,
    val productPhotoRepository: ProductPhotoRepository,
    val mstProductRepository: MstProductRepository
) {
    @PostMapping("/addProductPhoto")
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
    @GetMapping("getPhoto/{productId}")
    fun getProductPhoto(@PathVariable productId: Int): ResponseEntity<BaseResponse<List<PhotoResponse>>> {
        val photo = mstProductRepository.findByProductId(productId).get()
        return productPhotoService.getPhotoByProductId(photo)
    }

    @DeleteMapping("deletePhoto/{productId}")
    fun deleteProductPhoto(@PathVariable productId: Int): ResponseEntity<BaseResponse<Any>> {
        val photo = productPhotoRepository.findById(productId)
        if (photo.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Product's id not found",
                    data = null
                )
            )
        }
        return productPhotoService.deletePhoto(photo.get().productPhotoId!!)
    }
}