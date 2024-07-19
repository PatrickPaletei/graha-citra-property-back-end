package com.citra.graha.controller

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
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product Photo API", description = "API for managing photo")
class ProductPhotoController(
    val productPhotoService: ProductPhotoService,
    val productPhotoRepository: ProductPhotoRepository,
    val productRepository: ProductRepository
) {
    @PostMapping("/addProductPhotoOrVideo/{productId}")
    @Operation(summary = "Create Product photo", description = "buat nambahin foto produk file ditaruh di body pake form-data")
    fun createProductPhoto(@PathVariable("productId") productId: Int,
                           @RequestParam("photo") photo: List<MultipartFile>
    ): ResponseEntity<BaseResponse<Any>> {
        val existProductId = productRepository.findByProductId(productId)

        if (existProductId.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Product's id not found",
                    data = null
                )
            )
        }

        return productPhotoService.addPhotoOrVideo(photo,existProductId.get())

    }

    @GetMapping("getPhotoOrVideo/{productId}")
    @Operation(summary = "Get All Product Photo or Video By Id", description = "buat ambil semua foto atau video produk sesuai product ID")
    fun getProductPhoto(@PathVariable productId: Int): ResponseEntity<BaseResponse<List<PhotoResponse>>> {
        val photo = productRepository.findByProductId(productId)
        if (photo.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Product with product id $productId not exist",
                    data = null
                )
            )
        }
        return productPhotoService.getPhotoOrVideoByProductId(photo.get())
    }

    @GetMapping("/loadPhotoOrVideo/{fileName:.+}")
    @Operation(summary = "Load Product Photo or video By filePath", description = "Load photo or video by file name")
    fun loadPhoto(@PathVariable fileName: String): ResponseEntity<Any> {
        return productPhotoService.loadPhoto(fileName)
    }

    @DeleteMapping("deletePhoto/{idProductPhoto}")
    @Operation(summary = "Delete Product Photo", description = "buat hapus foto produk")
    fun deleteProductPhoto(@PathVariable idProductPhoto: Int): ResponseEntity<BaseResponse<Any>> {
        val photo = productPhotoRepository.findById(idProductPhoto)
        if (photo.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "idProductPhoto not found",
                    data = null
                )
            )
        }
        return productPhotoService.deletePhoto(photo.get().productPhotoId!!)
    }
}