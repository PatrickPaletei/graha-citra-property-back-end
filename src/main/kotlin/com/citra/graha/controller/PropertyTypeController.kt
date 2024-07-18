package com.citra.graha.controller

import com.citra.graha.dto.request.UpdatePropertyTypeRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PropertyTypeResponse
import com.citra.graha.entity.MstPropertyType
import com.citra.graha.repository.ProductRepository
import com.citra.graha.repository.PropertyTypeRepository
import com.citra.graha.service.PropertyTypeService
import io.swagger.v3.oas.annotations.Operation
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
        description = "buat nambahin Property Type ")
    fun addPropertyType(@PathVariable propertyType: String): ResponseEntity<BaseResponse<Any>> {
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
    @Operation(summary = "Get All Property Type", description = "buat ambil semua Property Type")
    fun getAllPropertyTypes(): ResponseEntity<BaseResponse<List<PropertyTypeResponse>>> {
        return propertyTypeService.getPropertyTypes()
    }

    @DeleteMapping("/removePropertyType/{propertyId}")
    @Operation(summary = "Delete Property Type",
        description = "buat hapus Property Type")
    fun removePropertyTypes(@PathVariable propertyId: Int): ResponseEntity<BaseResponse<Any>> {
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
        if (existInMstProperty.isPresent) {
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
    @Operation(summary = "Update Property Type")
    fun updatePropertyType(@RequestBody newPropertyName: UpdatePropertyTypeRequest): ResponseEntity<BaseResponse<Any>> {
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