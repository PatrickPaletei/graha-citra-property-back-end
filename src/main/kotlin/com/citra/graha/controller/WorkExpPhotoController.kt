package com.citra.graha.controller

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.dto.response.WorkExpPhotoResponse
import com.citra.graha.entity.WorkExpPhoto
import com.citra.graha.repository.WorkExpPhotoRepository
import com.citra.graha.repository.WorkExperienceRepository
import com.citra.graha.service.WorkExpPhotoService
import io.swagger.v3.oas.annotations.Operation
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
    @Operation(summary = "Create work exp photo or video", description = "buat nambahin foto work exp, file ditaruh di body pake form-data")
    fun createWorkExpPhoto(
        @PathVariable("workExpId") workExpId: Int,
        @RequestParam("photo") photo: List<MultipartFile>
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
    @Operation(summary = "Get All work exp Photo or Video By Id", description = "buat ambil semua foto atau video work exp sesuai product ID")
    fun getWorkExpPhoto(@PathVariable("workExpId") workExpId: Int): ResponseEntity<BaseResponse<List<WorkExpPhotoResponse>>> {
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
    @Operation(summary = "Load work exp Photo or video By fileName", description = "Load photo or video by file name")
    fun loadPhoto(@PathVariable fileName: String): ResponseEntity<Any> {
        return workExpPhotoService.loadPhoto(fileName)
    }


}