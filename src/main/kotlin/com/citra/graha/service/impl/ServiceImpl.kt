package com.citra.graha.service.impl

import com.citra.graha.dto.request.AddServiceRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstService
import com.citra.graha.repository.ServiceRepository
import com.citra.graha.service.ServiceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ServiceImpl(
    val serviceRepository: ServiceRepository
) : ServiceService {
    override fun addService(service: AddServiceRequest): ResponseEntity<BaseResponse<Any>> {
        val existService = serviceRepository.findByServiceName(service.serviceName)
        if (existService.isPresent) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Service already exist",
                    data = null
                )
            )
        }

        val service = MstService(
            serviceName = service.serviceName,
            serviceDescription = service.serviceDescription
        )
        serviceRepository.save(service)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Success add service",
                data = service
            )
        )
    }

}