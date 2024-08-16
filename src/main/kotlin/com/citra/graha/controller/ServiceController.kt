package com.citra.graha.controller

import com.citra.graha.dto.request.AddServiceRequest
import com.citra.graha.dto.request.DeleteRequest
import com.citra.graha.dto.request.UpdateServiceRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstService
import com.citra.graha.repository.ServiceRepository
import com.citra.graha.repository.WorkExperienceRepository
import com.citra.graha.service.ServiceService
import com.citra.graha.util.ApiDocumentation
import com.citra.graha.util.swaggerschema.BaseResponseWithMstService
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
    val serviceRepository: ServiceRepository,
    val workExperienceRepository: WorkExperienceRepository
) {

    @PostMapping("/addService")
    @Operation(summary = "Create New Service",
        description = "semua field untuk menambahkan service tidak boleh null",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "Add service Request Example",
                            value = ApiDocumentation.Request.ADD_SERVICE_REQUEST_EXAMPLE
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "400",
                description = "status = F. possible messages:\n " +
                        "1. Make sure all parameter is not null\n " +
                        "2. Service already exist",
                content = [
                    Content(
                        mediaType = "applicaition/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Success add Service",
                content = [
                    Content(
                        mediaType = "applicaition/json",
                        schema = Schema(implementation = BaseResponseWithMstService::class)
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

    @GetMapping("getServiceById/{id}")
    @Operation(summary = "Get Service by id",
        description = "buat get service pake id",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Get service by id",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstService::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Service not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun getServiceById(@Parameter(description = "id dari service yang mau diambil datanya") @PathVariable id: String): ResponseEntity<BaseResponse<MstService>> {
        return serviceService.getServiceById(Integer.valueOf(id))
    }

    @GetMapping("/allservices")
    @Operation(summary = "Get Al Service",
        description = "buat get all service",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Get all services",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstService::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = No services found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun getAllServices(): ResponseEntity<BaseResponse<List<MstService>>> {
        return serviceService.getAllServices()
    }

    @DeleteMapping("/{idService}")
    @Operation(summary = "Delete Service",
        description = "buat delete service",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Service not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "403",
                description = "status = F. message = Service with id <idService> is used",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Service deleted",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun deleteService(@Parameter(description = "id user yang akan dihapus", required = true) @PathVariable idService: Int): ResponseEntity<BaseResponse<Any>> {
        val service = serviceRepository.findById(idService)
        if (service.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Service not found",
                    data = null
                )
            )
        }

        val existInWorkExperience = workExperienceRepository.findByServiceId(service.get())
        if (existInWorkExperience.isNotEmpty()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                BaseResponse(
                    status = "F",
                    message = "Service with id $idService is used",
                    data = null
                )
            )
        }
        return serviceService.deleteService(service.get())
    }

    @PutMapping
    @Operation(
        summary = "Update Service",
        description = "buat update service",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "update service name",
                            value = ApiDocumentation.Request.UPDATE_SERVICE_REQUEST_SERVICE_NAME
                        ),
                        ExampleObject(
                            name = "update service description",
                            value = ApiDocumentation.Request.UPDATE_SERVICE_REQUEST_SERVICE_DESCRIPTION
                        ),
                        ExampleObject(
                            name = "update service description and name",
                            value = ApiDocumentation.Request.UPDATE_SERVICE_REQUEST_SERVICE_DESCRIPTION_NAME
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "400",
                description = "status = F. possible messages:\n " +
                        "1. Please input the service id\n " +
                        "2. Service name is already exist",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Service not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = F. message = Service updated successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstService::class)
                    )
                ]
            )
        ]
    )
    fun updateService(@RequestBody service: UpdateServiceRequest): ResponseEntity<BaseResponse<MstService>> {
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