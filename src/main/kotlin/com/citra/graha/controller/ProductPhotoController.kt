package com.citra.graha.controller

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.repository.ProductRepository
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.service.ProductPhotoService
import com.citra.graha.util.swaggerschema.NullResponse
import com.citra.graha.util.swaggerschema.productphoto.BaseResponseWithListPhotoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/productPhoto")
@Tag(name = "Product Photo API", description = "API for managing photo")
class ProductPhotoController(
    val productPhotoService: ProductPhotoService,
    val productPhotoRepository: ProductPhotoRepository,
    val productRepository: ProductRepository
) {
    @PostMapping("/addProductPhotoOrVideo/{productId}")
    @Operation(
        summary = "Create Product photo",
        description = "buat nambahin foto produk file ditaruh di body pake form-data",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Product's id not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "status = F. message = Please select at least one file to upload",
                content = [
                    Content(
                        mediaType = "application/type",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "status = F. message = Failed to upload photos or video",
                content = [
                    Content(
                        mediaType = "application/type",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Photos or video added successfully",
                content = [
                    Content(
                        mediaType = "application/type",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun createProductPhoto(@Parameter(description = "id product yang mau ditambahin foto", required = true) @PathVariable("productId") productId: Int,
                           @Parameter(description = "photo untuk product", required = true) @RequestParam("photo") photo: List<MultipartFile>
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
    @Operation(
        summary = "Get All Product Photo or Video By Id",
        description = "buat ambil semua foto atau video produk sesuai product ID",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. possible messages: \n" +
                        "1. Product with product id <productId> not exist \n" +
                        "2. No photo found for product id <productId>",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Photos retrieved successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListPhotoResponse::class)
                    )
                ]
            )
        ]
    )
    fun getProductPhoto(@Parameter(description = "id product yang ingin diambil foto atau videonya") @PathVariable productId: Int): ResponseEntity<BaseResponse<List<PhotoResponse>>> {
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
    @Operation(summary = "Load Product Photo or video By fileName", description = "Load photo or video by file name")
    fun loadPhoto(@PathVariable fileName: String): ResponseEntity<Any> {
        return productPhotoService.loadPhoto(fileName)
    }

    @DeleteMapping("deletePhoto/{idProductPhoto}")
    @Operation(
        summary = "Delete Product Photo",
        description = "buat hapus foto produk",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. possible messages: \n" +
                        "1. idProductPhoto not found \n" +
                        "2. File not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Photo deleted successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "status = F. message = Failed to delete photo",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun deleteProductPhoto(@Parameter(description = "id foto produk yang mau dihapus", required = true) @PathVariable idProductPhoto: Int): ResponseEntity<BaseResponse<Any>> {
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
        val deleteReturn = productPhotoService.deletePhoto(photo.get().productPhotoId!!)
        return ResponseEntity.status(deleteReturn.statusCode).body(
            BaseResponse(
                status = deleteReturn.statusString,
                message = deleteReturn.message
            )
        )
    }
}