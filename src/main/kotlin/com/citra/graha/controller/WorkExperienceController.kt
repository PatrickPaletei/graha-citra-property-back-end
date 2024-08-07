package com.citra.graha.controller

import com.citra.graha.dto.request.AddWorkExperience
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstWorkExperience
import com.citra.graha.repository.ServiceRepository
import com.citra.graha.repository.WorkExperienceRepository
import com.citra.graha.service.WorkExperienceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/workexperience")
class WorkExperienceController(
    val workExperienceService: WorkExperienceService,
    val serviceRepository: ServiceRepository,
    val workExperienceRepository: WorkExperienceRepository
) {
    @PostMapping
    fun addWorkExperience(@RequestBody addWorkExperience: AddWorkExperience): ResponseEntity<BaseResponse<MstWorkExperience>>{
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
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
    fun searchWorkExperience(@RequestParam(name = "searchValue", defaultValue = "") searchValue: String): ResponseEntity<BaseResponse<List<MstWorkExperience>>>{
        return workExperienceService.searchWorkExperience(searchValue)
    }

    @GetMapping("/all")
    fun getAllWorkExperience(): ResponseEntity<BaseResponse<List<MstWorkExperience>>>{
        return workExperienceService.getAllWorkExperiences();
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<BaseResponse<MstWorkExperience>>{
        return workExperienceService.getWorkExperience(id)
    }

    @DeleteMapping("/{id}")
    fun deleteWorkExperience(@PathVariable id: Int): ResponseEntity<BaseResponse<Any>>{
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
        return workExperienceService.deleteWorkExperience(workExperience.get())
    }

    @PutMapping
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