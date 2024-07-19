package com.citra.graha.service

import com.citra.graha.dto.request.AddServiceRequest
import com.citra.graha.dto.request.UpdateServiceRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstService
import org.springframework.http.ResponseEntity

interface ServiceService {
    fun addService(service: AddServiceRequest): ResponseEntity<BaseResponse<MstService>>
    fun getServiceById(id: Int): ResponseEntity<BaseResponse<MstService>>
    fun getAllServices(): ResponseEntity<BaseResponse<List<MstService>>>
    fun deleteService(service: MstService): ResponseEntity<BaseResponse<Any>>
    fun updateService(existService: MstService, updateService: UpdateServiceRequest): ResponseEntity<BaseResponse<MstService>>
}