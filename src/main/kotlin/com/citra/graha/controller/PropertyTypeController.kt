package com.citra.graha.controller

import com.citra.graha.dto.request.UpdatePropertyTypeRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PropertyTypeResponse
import com.citra.graha.entity.MstPropertyType
import com.citra.graha.repository.ProductRepository
import com.citra.graha.repository.PropertyTypeRepository
import com.citra.graha.service.PropertyTypeService
import com.citra.graha.util.ApiDocumentation
import com.citra.graha.util.swaggerschema.BaseResponseWithListPropertyTypeResponse
import com.citra.graha.util.swaggerschema.BaseResponseWithMstPropertyType
import com.citra.graha.util.swaggerschema.NullResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/propertyType")
@Tag(name = "Property Type API", description = "API for managing Property Type")
class PropertyTypeController(
    val propertyTypeRepository: PropertyTypeRepository,
    val propertyTypeService: PropertyTypeService,
    val productRepository: ProductRepository
) {
    @PostMapping("/addPropertyType/{propertyType}")
    @Operation(summary = "Create Property Type",
        description = "buat nambahin Property Type",
        responses = [
            ApiResponse(
                responseCode = "409",
                description = "status = F. message = Property with name <property type> already exists",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Property Type added successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstPropertyType::class)
                    )
                ]
            )
        ]
    )
    fun addPropertyType(@Parameter(description = "nama property yang ingin ditambahkan", required = true) @PathVariable propertyType: String): ResponseEntity<BaseResponse<MstPropertyType>> {
        val existPropertyType = propertyTypeRepository.findByPropertyNameIgnoreCase(propertyType)
        if (existPropertyType.isPresent) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                BaseResponse(
                    status = "F",
                    message = "Property with name ${existPropertyType.get()} already exists",
                    data = null
                )
            )
        }
        return propertyTypeService.addPropertyType(propertyType)
    }

    @GetMapping("/getAllPropertyType")
    @Operation(
        summary = "Get All Property Type",
        description = "buat ambil semua Property Type",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = No Property Type found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Get All Property Types success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListPropertyTypeResponse::class)
                    )
                ]
            )
        ]
    )
    fun getAllPropertyTypes(): ResponseEntity<BaseResponse<List<PropertyTypeResponse>>> {
        return propertyTypeService.getPropertyTypes()
    }

    @DeleteMapping("/removePropertyType/{propertyId}")
    @Operation(summary = "Delete Property Type",
        description = "buat hapus Property Type",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Property Type with id <propertyId> not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "403",
                description = "status = F. message = Property Type with id <propertyId> is used",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Property Type with id <propertyTypeId> deleted",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun removePropertyTypes(@Parameter(description = "id property type yang mau dihapus") @PathVariable propertyId: Int): ResponseEntity<BaseResponse<Any>> {
        val mstProperty = MstPropertyType(
            propertyId = propertyId,
            propertyName = ""
        )
        val existInMstProperty = productRepository.findByPropertyId(mstProperty)
        val propertyType = propertyTypeRepository.findById(propertyId)
        if (propertyType.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Property Type with id $propertyId not found",
                    data = null
                )
            )
        }
        if (existInMstProperty.isNotEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                BaseResponse(
                    status = "F",
                    message = "Property Type with id $propertyId is used",
                    data = null
                )
            )
        }
        return propertyTypeService.deletePropertyType(propertyType.get().propertyId!!)
    }

    @PutMapping("/updatePropertyType")
    @Operation(summary = "Update Property Type", description = "Melakukan update data pada Property Type",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "Contoh request untuk update property type",
                            description = "all fields cannot be null",
                            value = ApiDocumentation.Request.UPDATE_PROPERTY_TYPE_REQUEST_EXAMPLE
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Property with name <propertyName> not found",
                content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "409",
                description = "status = F. message = Property with name <propertyName> cannot be same",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Property Type updated",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListPropertyTypeResponse::class)
                    )
                ]
            )
        ]
    )
    fun updatePropertyType(@RequestBody newPropertyName: UpdatePropertyTypeRequest): ResponseEntity<BaseResponse<PropertyTypeResponse>> {
        val existPropertyName = propertyTypeRepository.findById(Integer.valueOf(newPropertyName.propertyId))
        val currentPropertyName = propertyTypeRepository.findByPropertyNameIgnoreCase(newPropertyName.propertyName)
        if (existPropertyName.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Property with name ${newPropertyName.propertyName} not found",
                    data = null
                )
            )
        }
        if (currentPropertyName.isPresent) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                BaseResponse(
                    status = "F",
                    message = "Property with name ${newPropertyName.propertyName} cannot be same",
                    data = null
                )
            )
        }
        return propertyTypeService.updatePropertyType(
            existPropertyName.get(),
            newPropertyName.propertyId,
            newPropertyName.propertyName
        )

    }


}