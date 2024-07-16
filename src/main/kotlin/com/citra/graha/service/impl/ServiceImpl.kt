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
    override fun addService(service: AddServiceRequest): ResponseEntity<BaseResponse<MstService>> {
        val service = MstService(
            serviceName = service.serviceName.toString(),
            serviceDescription = service.serviceDescription.toString()
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

    override fun getServiceById(id: Int): ResponseEntity<BaseResponse<MstService>> {
        val service = serviceRepository.findById(id)
        if (service.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Service not found",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get service by id",
                data = service.get()
            )
        )
    }

    override fun getAllServices(): ResponseEntity<BaseResponse<List<MstService>>> {
        val allServices = serviceRepository.findAll()
        if (allServices.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "No services found",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get all services",
                data = allServices
            )
        )
    }

    override fun deleteService(service: MstService): ResponseEntity<BaseResponse<Any>> {
        serviceRepository.delete(service)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Service deleted",
                data = null
            )
        )
    }

    override fun updateService(
        existService: MstService,
        updateService: AddServiceRequest
    ): ResponseEntity<BaseResponse<MstService>> {
        val updatedService = existService.copy(
            serviceName = updateService.serviceName ?: existService.serviceName,
            serviceDescription = updateService.serviceDescription ?: existService.serviceDescription
        )

        serviceRepository.save(updatedService)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Service updated successfully",
                data = updatedService
            )
        )
    }

}