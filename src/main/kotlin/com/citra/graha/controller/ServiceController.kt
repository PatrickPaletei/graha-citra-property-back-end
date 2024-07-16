package com.citra.graha.controller

import com.citra.graha.dto.request.AddServiceRequest
import com.citra.graha.dto.request.DeleteRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstService
import com.citra.graha.repository.ServiceRepository
import com.citra.graha.service.ServiceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/service")
class ServiceController(
    val serviceService: ServiceService,
    val serviceRepository: ServiceRepository
) {

    @PostMapping
    fun addService(@RequestBody service: AddServiceRequest): ResponseEntity<BaseResponse<MstService>> {
        if (service.serviceName == null || service.serviceDescription == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Make sure all parameter is not null",
                    data = null
                )
            )
        }
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
        return serviceService.addService(service)
    }

    @GetMapping("/{id}")
    fun getServiceById(@PathVariable id: String): ResponseEntity<BaseResponse<MstService>>{
        return serviceService.getServiceById(Integer.valueOf(id))
    }

    @GetMapping("/allservices")
    fun getAllServices(): ResponseEntity<BaseResponse<List<MstService>>>{
        return serviceService.getAllServices()
    }

    @DeleteMapping
    fun deleteService(@RequestBody deleteRequest: DeleteRequest): ResponseEntity<BaseResponse<Any>>{
        val service = serviceRepository.findById(deleteRequest.id)
        if (service.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Service not found",
                    data = null
                )
            )
        }
        return serviceService.deleteService(service.get())
    }

    @PutMapping
    fun updateService(@RequestBody service: AddServiceRequest): ResponseEntity<BaseResponse<MstService>>{
        if (service.id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Please input the service id",
                    data = null
                )
            )
        }
        val existService = serviceRepository.findById(Integer.valueOf(service.id))
        if (service.serviceName != null && existService.get().serviceName != service.serviceName){
            val existName = serviceRepository.findByServiceName(service.serviceName)
            if (existName.isPresent){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    BaseResponse(
                        status = "F",
                        message = "Service name is already exist",
                        data = null
                    )
                )
            }
        }
        if (existService.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Service not found",
                    data = null
                )
            )
        }
        return serviceService.updateService(existService.get(), service)
    }
}