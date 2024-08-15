package com.citra.graha.service.impl

import com.citra.graha.dto.request.AddWorkExperience
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstService
import com.citra.graha.entity.MstWorkExperience
import com.citra.graha.repository.WorkExperienceRepository
import com.citra.graha.service.WorkExperienceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class WorkExperienceImpl(
    private val workExperienceRepository: WorkExperienceRepository
): WorkExperienceService {
    override fun createWorkExperience(
        experience: AddWorkExperience,
        service: MstService
    ): ResponseEntity<BaseResponse<MstWorkExperience>> {
        val workExperience = MstWorkExperience(
            name = experience.name.toString(),
            description = experience.description.toString(),
            serviceId = service
        )
        workExperienceRepository.save(workExperience)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            BaseResponse(
                status = "T",
                message = "Work experience created",
                data = workExperience
            )
        )
    }

    override fun searchWorkExperience(searchValue: String): ResponseEntity<BaseResponse<List<MstWorkExperience>>> {
        val workExperiences = workExperienceRepository.searchWorkExperience(searchValue)
        if (workExperiences.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Work experience not found",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Search work experience",
                data = workExperiences
            )
        )
    }

    override fun getWorkExperience(id: Int): ResponseEntity<BaseResponse<MstWorkExperience>> {
        val workExperienceById = workExperienceRepository.findById(id)
        if (workExperienceById.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Work experience not found",
                    data = null
                )
            )
        }

        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get work experience",
                data = workExperienceById.get()
            )
        )

    }

    override fun getAllWorkExperiences(): ResponseEntity<BaseResponse<List<MstWorkExperience>>> {
        var workExperience = workExperienceRepository.findAll()
        if (workExperience.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Work experience not found",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get all work experience",
                data = workExperience
            )
        )
    }

    override fun deleteWorkExperience(workExperience: MstWorkExperience): ResponseEntity<BaseResponse<Any>> {
        workExperienceRepository.delete(workExperience)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Work experience deleted",
                data = null
            )
        )
    }

    override fun updateWorkExperience(
        existWorkExperience: MstWorkExperience,
        updateExperience: AddWorkExperience,
        service: MstService?
    ): ResponseEntity<BaseResponse<MstWorkExperience>> {
        val updatedExperience = existWorkExperience.copy(
            name = updateExperience.name ?: existWorkExperience.name,
            description = updateExperience.description ?: existWorkExperience.description,
            serviceId = service ?: existWorkExperience.serviceId
        )

        workExperienceRepository.save(updatedExperience)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Work experience updated",
                data = updatedExperience
            )
        )
    }
}