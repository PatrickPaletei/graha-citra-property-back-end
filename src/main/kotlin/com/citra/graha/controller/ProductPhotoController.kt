package com.citra.graha.controller

import com.citra.graha.dto.request.AddPhotoRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.repository.ProductRepository
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.service.ProductPhotoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product Photo API", description = "API for managing photo")
class ProductPhotoController(
    val productPhotoService: ProductPhotoService,
    val productPhotoRepository: ProductPhotoRepository,
    val productRepository: ProductRepository
) {
    @PostMapping("/addProductPhoto")
    @Operation(summary = "Create Product photo", description = "buat nambahin foto produk")
    fun createProductPhoto(@RequestBody productPhoto: AddPhotoRequest): ResponseEntity<BaseResponse<Any>> {

        val existProductId = productRepository.findByProductId(productPhoto.productId)
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
    @Operation(summary = "Get All Product Photo", description = "buat ambil semua foto produk")
    fun getProductPhoto(@PathVariable productId: Int): ResponseEntity<BaseResponse<List<PhotoResponse>>> {
        val photo = productRepository.findByProductId(productId).get()
        return productPhotoService.getPhotoByProductId(photo)
    }

    @DeleteMapping("deletePhoto/{productId}")
    @Operation(summary = "Delete Product Photo", description = "buat hapus foto produk")
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