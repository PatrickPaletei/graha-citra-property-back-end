package com.citra.graha.controller

import com.citra.graha.dto.request.AddServiceRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.service.ServiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/service")
class ServiceController(
    val serviceService: ServiceService
) {

    @PostMapping
    fun addService(@RequestBody service: AddServiceRequest): ResponseEntity<BaseResponse<Any>> {
        return serviceService.addService(service)
    }
}