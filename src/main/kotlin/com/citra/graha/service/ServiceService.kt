package com.citra.graha.service

import com.citra.graha.dto.request.AddServiceRequest
import com.citra.graha.dto.response.BaseResponse
import org.springframework.http.ResponseEntity

interface ServiceService {
    fun addService(service: AddServiceRequest): ResponseEntity<BaseResponse<Any>>
}