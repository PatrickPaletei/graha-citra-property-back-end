package com.citra.graha.controller

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.dto.response.WorkExpPhotoResponse
import com.citra.graha.entity.WorkExpPhoto
import com.citra.graha.repository.WorkExpPhotoRepository
import com.citra.graha.repository.WorkExperienceRepository
import com.citra.graha.service.WorkExpPhotoService
import com.citra.graha.util.swaggerschema.BaseResponseWithListWorkExpPhotoResponse
import com.citra.graha.util.swaggerschema.NullResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/workExpPhoto")
@Tag(name = "Work Exp Photo", description = "API for managing work exp photo")
class WorkExpPhotoController(
    val workExpPhotoService: WorkExpPhotoService,
    val workExpPhotoRepository: WorkExpPhotoRepository,
    val workExpRepository: WorkExperienceRepository
) {

    @PostMapping("/addWorkExpPhotoOrVideo/{workExpId}")
    @Operation(
        summary = "Create work exp photo or video",
        description = "buat nambahin foto work exp, file ditaruh di body pake form-data",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Work Exp id's not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "status = F. message = Please select at least one file to upload",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = F. message = Photos or video added successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "status = F. message = Failed to upload photos or video",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun createWorkExpPhoto(
        @Parameter(description = "id dari work exp yang mau ditambah fotonya", required = true) @PathVariable("workExpId") workExpId: Int,
        @Parameter(description = "file photo yang ingin disimpan untuk id work exp", required = true) @RequestParam("photo") photo: List<MultipartFile>
    ): ResponseEntity<BaseResponse<Any>> {
        val existWorkExpId = workExpRepository.findById(workExpId)
        if (existWorkExpId.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Work Exp id's not found",
                    data = null
                )
            )
        }
        return workExpPhotoService.addPhotoOrVideo(photo,existWorkExpId.get())
    }

    @GetMapping("/getPhotoOrVideo/{workExpId}")
    @Operation(
        summary = "Get All work exp Photo or Video By Id",
        description = "buat ambil semua foto atau video work exp sesuai product ID",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. possible message:\n " +
                        "1. photos with work exp id <workExpId> not exist\n " +
                        "2. No photo found for product id <idWorkExp>",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Photos retrieved successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListWorkExpPhotoResponse::class)
                    )
                ]
            )
        ]
    )
    fun getWorkExpPhoto(@Parameter(description = "id work exp yang ingin diambil datanya", required = true) @PathVariable("workExpId") workExpId: Int): ResponseEntity<BaseResponse<List<WorkExpPhotoResponse>>> {
        val photo = workExpRepository.findById(workExpId)
        if (photo.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "photos with work exp id $workExpId not exist",
                    data = null
                )
            )
        }
        return workExpPhotoService.getPhotoOrVideoByProductId(photo.get())
    }

    @GetMapping("/loadPhotoOrVideo/{fileName:.+}")
    @Operation(
        summary = "Load work exp Photo or video By fileName",
        description = "Load photo or video by file name"
    )
    fun loadPhoto(@Parameter(description = "fileName photo/video yang ingin di-load") @PathVariable fileName: String): ResponseEntity<Any> {
        return workExpPhotoService.loadPhoto(fileName)
    }

    @DeleteMapping("/deletePhoto/{idWorkExpPhoto}")
    @Operation(
        summary = "menghapus foto work exp",
        description = "menghapus foto pada work exp berdasarkan dari id work exp photo",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. possible messages:\n " +
                        "1. idWorkExpPhoto not found\n " +
                        "2. File not found\n ",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "status = F. message = Failed to delete photo",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Photo deleted successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun deleteWorkExpPhoto(@Parameter(description = "id work exp photo yang mau dihapus") @PathVariable idWorkExpPhoto: Int): ResponseEntity<BaseResponse<Any>>{
        val photo = workExpPhotoRepository.findById(idWorkExpPhoto)
        if(photo.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "idWorkExpPhoto not found",
                    data = null
                )
            )
        }
        val deleteReturn = workExpPhotoService.deletePhoto(photo.get().workExpPhotoId!!)
        return ResponseEntity.status(deleteReturn.statusCode).body(
            BaseResponse(
                status = deleteReturn.statusString,
                message = deleteReturn.message
            )
        )
    }

}