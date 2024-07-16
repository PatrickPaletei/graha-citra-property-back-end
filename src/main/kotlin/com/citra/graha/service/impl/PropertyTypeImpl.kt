package com.citra.graha.service.impl

import com.citra.graha.dto.request.UpdatePropertyTypeRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PropertyTypeResponse
import com.citra.graha.entity.MstPropertyType
import com.citra.graha.repository.PropertyTypeRepository
import com.citra.graha.service.PropertyTypeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PropertyTypeImpl(
    val propertyTypeRepository: PropertyTypeRepository
) : PropertyTypeService {
    override fun addPropertyType(propertyType: String): ResponseEntity<BaseResponse<Any>> {
        val propertyType = MstPropertyType(
            propertyName = propertyType
        )
        propertyTypeRepository.save(propertyType)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Property Type added successfully"
            )
        )
    }

    override fun getPropertyTypes(): ResponseEntity<BaseResponse<List<PropertyTypeResponse>>> {
        val allPropertyTypes = propertyTypeRepository.findAll()
        if (allPropertyTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "No Property Type found",
                    data = null
                )
            )
        }
        val listPropertyType = mutableListOf<PropertyTypeResponse>()
        allPropertyTypes.forEach {
            listPropertyType.add(
                PropertyTypeResponse(
                    propertyTypeId = it.propertyId!!,
                    propertyName = it.propertyName
                )
            )
        }
        return ResponseEntity.ok().body(
            BaseResponse(
                status = "T",
                message = "Get All Property Types success",
                data = listPropertyType
            )
        )
    }

    override fun deletePropertyType(propertyTypeId: Int): ResponseEntity<BaseResponse<Any>> {
        val propertyType = propertyTypeRepository.findById(propertyTypeId)
        propertyTypeRepository.delete(propertyType.get())
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Property Type with id $propertyTypeId deleted",
                data = null
            )
        )
    }

    override fun updatePropertyType(
        mstPropertyType: MstPropertyType,
        propertyTypeId: Int,
        propertyTypeName: String
    ): ResponseEntity<BaseResponse<Any>> {
        val updatedPropertyType = mstPropertyType.copy(propertyName = propertyTypeName)
        propertyTypeRepository.save(updatedPropertyType)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Property Type updated",
                data = UpdatePropertyTypeRequest(
                    propertyId = propertyTypeId,
                    propertyName = propertyTypeName
                )
            )
        )
    }
}