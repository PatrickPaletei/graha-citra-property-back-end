package com.citra.graha.controller

import com.citra.graha.dto.request.AddWorkExperience
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstWorkExperience
import com.citra.graha.repository.ServiceRepository
import com.citra.graha.repository.WorkExpPhotoRepository
import com.citra.graha.repository.WorkExperienceRepository
import com.citra.graha.service.WorkExpPhotoService
import com.citra.graha.service.WorkExperienceService
import com.citra.graha.util.ApiDocumentation
import com.citra.graha.util.swaggerschema.BaseResponseWithListMstWorkExperience
import com.citra.graha.util.swaggerschema.BaseResponseWithMstWorkExperience
import com.citra.graha.util.swaggerschema.NullResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Files

@RestController
@RequestMapping("/api/workexperience")
class WorkExperienceController(
    val workExperienceService: WorkExperienceService,
    val serviceRepository: ServiceRepository,
    val workExperienceRepository: WorkExperienceRepository,
    val workExpPhotoRepository: WorkExpPhotoRepository,
    val workExpPhotoService: WorkExpPhotoService
) {
    @PostMapping
    @Operation(
        summary = "menambahkan work experience",
        description = "menambahkan work experience berdasarkan service yang sudah ada. service id harus diisi",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "work experience request",
                            value = ApiDocumentation.Request.ADD_WORK_EXP_REQUEST_EXAMPLE
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "400",
                description = "status = F. possible messages:\n " +
                        "1. name and description cannot be null or empty\n " +
                        "2. serviceId cannot be null",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "201",
                description = "status = T. message = Work experience created",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstWorkExperience::class)
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
    fun addWorkExperience(@RequestBody addWorkExperience: AddWorkExperience): ResponseEntity<BaseResponse<MstWorkExperience>>{
        if (addWorkExperience.name == null || addWorkExperience.description == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "name and description cannot be null or empty",
                    data = null
                )
            )
        }
        if(addWorkExperience.serviceId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "serviceId cannot be null",
                    data = null
                )
            )
        }
        val service = serviceRepository.findById(addWorkExperience.serviceId)
        if (service.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Service not found",
                    data = null
                )
            )
        }
        return workExperienceService.createWorkExperience(addWorkExperience, service.get())
    }

    @GetMapping("/search")
    @Operation(
        summary = "melakukan pencarian work experience",
        description = "mencari work experience berdasarakan kata tertentu",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Search work experience",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListMstWorkExperience::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Work experience not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun searchWorkExperience(@Parameter(description = "value yang ingin dicari", required = true) @RequestParam(name = "searchValue", defaultValue = "") searchValue: String): ResponseEntity<BaseResponse<List<MstWorkExperience>>>{
        return workExperienceService.searchWorkExperience(searchValue)
    }

    @GetMapping("/all")
    @Operation(
        summary = "mendapatkan semua work experience",
        description = "mendapatkan semua work experience beserta service nya",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Get all work experience",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListMstWorkExperience::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Work experience not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun getAllWorkExperience(): ResponseEntity<BaseResponse<List<MstWorkExperience>>>{
        return workExperienceService.getAllWorkExperiences();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "work experience berdasarkan id",
        description = "mendapatkan work experience berdasarkan id tertentu",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Get work experience",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstWorkExperience::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Work experience not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun getById(@Parameter(description = "id work experience yang ingin didapatkan", required = true) @PathVariable id: Int): ResponseEntity<BaseResponse<MstWorkExperience>>{
        return workExperienceService.getWorkExperience(id)
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "menghapus work experience",
        description = "menghapus work experience berdasarkan id tertentu",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Work experience deleted",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = work experience not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "status = F. message = Failed to delete product, file not found or photo id not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun deleteWorkExperience(@Parameter(description = "id work experience yang ingin dihapus", required = true) @PathVariable id: Int): ResponseEntity<BaseResponse<Any>>{
        val workExperience = workExperienceRepository.findById(id)
        if (workExperience.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "work experience not found",
                    data = null
                )
            )
        }
        val listPhoto = workExpPhotoRepository.findByWorkExperienceId(workExperience.get())
        if(listPhoto.isEmpty()){
            return workExperienceService.deleteWorkExperience(workExperience.get())
        }
        val photoToDelete = workExpPhotoService.checkPhotoExist(listPhoto)
        if(!photoToDelete.canDelete){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                BaseResponse(
                    status = "F",
                    message = "Failed to delete product, file not found or photo id not found",
                    data = null
                )
            )
        }
        workExperienceRepository.deleteByIds(photoToDelete.listPhotoId!!)
        photoToDelete.listPhotoPath!!.forEach { photoPath ->
            if (Files.exists(photoPath)) {
                Files.delete(photoPath)
            }
        }
        return workExperienceService.deleteWorkExperience(workExperience.get())
    }

    @PutMapping
    @Operation(
        summary = "melakukan update work experience",
        description = "melakukan update work experience berdasarkan id work experience, jadi id wajib diisi tidak boleh null atau empty",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "update name",
                            value = ApiDocumentation.Request.UPDATE_WORK_EXP_REQUEST_NAME
                        ),
                        ExampleObject(
                            name = "update description",
                            value = ApiDocumentation.Request.UPDATE_WORK_EXP_REQUEST_DESCRIPTION
                        ),
                        ExampleObject(
                            name = "update name dan service",
                            value = ApiDocumentation.Request.UPDATE_WORK_EXP_REQUEST_NAME_SERVICE
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. possible messages:\n " +
                        "1. work experience not found\n " +
                        "2. service not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "status = F. message = id cannot be null",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Work experience updated",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstWorkExperience::class)
                    )
                ]
            )
        ]
    )
    fun updateWorkExperience(@RequestBody updateWorkExperience: AddWorkExperience): ResponseEntity<BaseResponse<MstWorkExperience>>{
        if (updateWorkExperience.id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "id cannot be null",
                    data = null
                )
            )
        }

        val existWorkExperience = workExperienceRepository.findById(updateWorkExperience.id)
        if(existWorkExperience.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "work experience not found",
                    data = null
                )
            )
        }

        if (updateWorkExperience.serviceId != null){
            val existService = serviceRepository.findById(updateWorkExperience.serviceId)
            if(existService.isEmpty){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    BaseResponse(
                        status = "F",
                        message = "service not found",
                        data = null
                    )
                )
            }
            return workExperienceService.updateWorkExperience(existWorkExperience.get(), updateWorkExperience, existService.get())
        }
        return workExperienceService.updateWorkExperience(existWorkExperience.get(), updateWorkExperience, null)

    }
}