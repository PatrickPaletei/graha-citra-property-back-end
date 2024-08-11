package com.citra.graha.controller

import com.citra.graha.dto.request.AddProductRequest
import com.citra.graha.dto.request.UpdateProductRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.MstPropertyType
import com.citra.graha.entity.MstStatus
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.repository.ProductRepository
import com.citra.graha.repository.PropertyTypeRepository
import com.citra.graha.repository.StatusRepository
import com.citra.graha.service.ProductPhotoService
import com.citra.graha.service.ProductService
import com.citra.graha.util.ApiDocumentation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.util.*

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product API", description = "API for managing product")
class ProductController(
    val productService: ProductService,
    val statusRepository: StatusRepository,
    val propertyTypeRepository: PropertyTypeRepository,
    val productRepository: ProductRepository,
    val productPhotoService: ProductPhotoService,
    val productPhotoRepository: ProductPhotoRepository
) {
    val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    @Operation(summary = "Create product", description = "buat nambahin product",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "All fields cannot be null",
                            value = ApiDocumentation.Request.ADD_PRODUCT_REQUEST_EXAMPLE
                        )
                    ]
                )
            ]
        ))
    fun addProduct(@RequestBody addProductRequest: AddProductRequest): ResponseEntity<BaseResponse<MstProduct>>{
        val status = statusRepository.findById(addProductRequest.statusId!!)
        if (status.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Status ${addProductRequest.statusId} not found"
                )
            )
        }

        val propertyType = propertyTypeRepository.findById(addProductRequest.propertyId!!)
        if (propertyType.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "propertyType ${addProductRequest.propertyId} not found"
                )
            )
        }
        return productService.addProduct(addProductRequest, propertyType.get(), status.get())
    }

    @DeleteMapping("/{idProduct}")
    @Operation(summary = "Delete product", description = "Menghapus product berdasarkan id")
    fun deleteProduct(@Parameter(description = "id of the product to be deleted") @PathVariable idProduct: Int): ResponseEntity<BaseResponse<Any>>{
        val existProduct = productRepository.findById(idProduct)
        if (existProduct.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Product with product id $idProduct not exist",
                    data = null
                )
            )
        }

        val listPhoto = productPhotoRepository.findByProductId(existProduct.get())
        if(listPhoto.isEmpty()){
            return productService.deleteProduct(existProduct.get())
        }
        val photoToDelete = productPhotoService.checkPhotosExist(listPhoto)
        if (!photoToDelete.canDelete){
            return ResponseEntity.ok(
                BaseResponse(
                    status = "F",
                    message = "Failed to delete product, file not found or photo id not found",
                    data = null
                )
            )
        }
        productPhotoRepository.deleteByIds(photoToDelete.listPhotoId!!)
        photoToDelete.listPhotoPath!!.forEach { photoPath ->
            if (Files.exists(photoPath)) {
                Files.delete(photoPath)
            }
        }
        return productService.deleteProduct(existProduct.get())
    }

    @GetMapping("/allProducts")
    @Operation(summary = "Get all products", description = "Mendapatkan semua product")
    fun getAllProducts(): ResponseEntity<BaseResponse<List<MstProduct>>>{
        return productService.getAll()
    }

    @GetMapping("/search")
    fun searchProduct(@RequestParam(name = "searchValue", defaultValue = "") searchValue: String): ResponseEntity<BaseResponse<List<MstProduct>>>{
        return productService.searchProduct(searchValue)
    }

    @GetMapping("/{idProduct}")
    @Operation(summary = "Get product by id", description = "Mendapatkan product berdasarkan id")
    fun getProductById(@Parameter(description = "id of the product to be fetch", required = true) @PathVariable idProduct: Int): ResponseEntity<BaseResponse<MstProduct>>{
        return productService.getById(idProduct)
    }

    @GetMapping("/propertyType")
    @Operation(summary = "Get product by property type name", description = "\"To get by propertyType you should put propertyTypeName or propertyTypeId, one of the value cannot be null or empty.")
    fun getByPropertyType(@RequestParam(name = "propertyTypeName", defaultValue = "") propertyTypeName: String,
                          @RequestParam(name = "propertyTypeId", defaultValue = "") propertyTypeId: String): ResponseEntity<BaseResponse<List<MstProduct>>>{
        if ("" == propertyTypeName && "" == propertyTypeId){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Please input the propertyTypeName or propertyTypeId"
                )
            )
        }
        return when {
            propertyTypeName != "" -> productService.getByPropertyTypeName(propertyTypeName)
            propertyTypeId != "" -> productService.getByPropertyTypeId(Integer.valueOf(propertyTypeId))
            else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Invalid request"
                )
            )
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Get product by status", description = "Mendapatkan product berdasarkan status name atau status id")
    fun getByStatus(@RequestParam(name = "statusName", defaultValue = "") statusName: String,
                        @RequestParam(name = "statusId", defaultValue = "") statusId: String): ResponseEntity<BaseResponse<List<MstProduct>>>{
        if ("" == statusName && "" == statusId){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Please input the statusName or statusId"
                )
            )
        }
        return when {
            statusName != "" -> productService.getByStatusName(statusName)
            statusId != "" -> productService.getByStatusId(statusId.toInt())
            else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Invalid request"
                )
            )
        }
    }

    @PutMapping
    @Operation(summary = "update product", description = "Melakukan update data pada Product",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "Product update request example",
                            description = "id is a must, fields other than id can be null",
                            value = ApiDocumentation.Request.UPDATE_PRODUCT_REQUEST_EXAMPLE
                        ),
                        ExampleObject(
                            name = "Product status update request example",
                            description = "id is a must",
                            value = ApiDocumentation.Request.UPDATE_PRODUCT_STATUS_REQUEST_EXAMPLE
                        ),
                        ExampleObject(
                            name = "Update some fields",
                            description = "id is a must",
                            value = ApiDocumentation.Request.UPDATE_PRODUCT_SOME_FIELDS_REQUEST_EXAMPLE
                        ),
                    ]
                )
            ]
        ))
    fun updateProduct(@RequestBody productData: UpdateProductRequest): ResponseEntity<BaseResponse<MstProduct>>{
        var existsPropertyType: Optional<MstPropertyType>? = null
        var existsStatus: Optional<MstStatus>? = null
        if (productData.id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Please input the product id",
                    data = null
                )
            )
        }
        val existProduct = productRepository.findById(productData.id)
        if (existProduct.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Product not found",
                    data = null
                )
            )
        }

        if (productData.statusId != null){
            existsStatus = statusRepository.findById(productData.statusId)
            if (existsStatus.isEmpty){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    BaseResponse(
                        status = "F",
                        message = "Status not found",
                        data = null
                    )
                )
            }
        }

        if (productData.propertyId != null){
            existsPropertyType = propertyTypeRepository.findById(productData.propertyId)
            if (existsPropertyType.isEmpty){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    BaseResponse(
                        status = "F",
                        message = "Property type not found",
                        data = null
                    )
                )
            }
        }

        return productService.updateProduct(existProduct.get(), productData, existsPropertyType?.get(), existsStatus?.get())
    }

}
