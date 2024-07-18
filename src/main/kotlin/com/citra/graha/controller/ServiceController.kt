package com.citra.graha.controller

import com.citra.graha.dto.request.AddServiceRequest
import com.citra.graha.dto.request.DeleteRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstService
import com.citra.graha.repository.ServiceRepository
import com.citra.graha.service.ServiceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Service API", description = "API for managing service")
class ServiceController(
    val serviceService: ServiceService,
    val serviceRepository: ServiceRepository
) {

    @PostMapping
    @Operation(
        summary = "Create New Service",
        description = "buat nambahin service",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "Add service Request Example",
                            value = """
                                {
                                    "serviceName": "Buat Kos",
                                    "serviceDescription": "Jasa Buat kos"
                                }
                            """
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponse::class),
                        examples = [
                            ExampleObject(
                                name = "Add Service Response Example",
                                value = """
                                    {
                                        "status": "T",
                                        "message": "Success add service",
                                        "data": {
                                            "idService": 2,
                                            "serviceName": "Buat Kos",
                                            "serviceDescription": "Jasa Buat Kos"
                                        }
}
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun addService(@RequestBody service: AddServiceRequest): ResponseEntity<BaseResponse<MstService>> {
        if (service.serviceName == null || service.serviceDescription == null) {
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
    @Operation(summary = "Get Service by id", description = "buat get service pake id")
    fun getServiceById(@PathVariable id: String): ResponseEntity<BaseResponse<MstService>> {
        return serviceService.getServiceById(Integer.valueOf(id))
    }

    @GetMapping("/allservices")
    @Operation(summary = "Get ALl Service", description = "buat get all service")
    fun getAllServices(): ResponseEntity<BaseResponse<List<MstService>>> {
        return serviceService.getAllServices()
    }

    @DeleteMapping
    @Operation(summary = "Delete Service", description = "buat delete service")
    fun deleteService(@RequestBody deleteRequest: DeleteRequest): ResponseEntity<BaseResponse<Any>> {
        val service = serviceRepository.findById(deleteRequest.id)
        if (service.isEmpty) {
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
    @Operation(summary = "Update Service", description = "buat update service")
    fun updateService(@RequestBody service: AddServiceRequest): ResponseEntity<BaseResponse<MstService>> {
        if (service.id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Please input the service id",
                    data = null
                )
            )
        }
        val existService = serviceRepository.findById(Integer.valueOf(service.id))
        if (service.serviceName != null && existService.get().serviceName != service.serviceName) {
            val existName = serviceRepository.findByServiceName(service.serviceName)
            if (existName.isPresent) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    BaseResponse(
                        status = "F",
                        message = "Service name is already exist",
                        data = null
                    )
                )
            }
        }
        if (existService.isEmpty) {
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