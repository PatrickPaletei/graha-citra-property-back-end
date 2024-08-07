package com.citra.graha.service

import com.citra.graha.dto.request.AddWorkExperience
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstService
import com.citra.graha.entity.MstWorkExperience
import org.springframework.http.ResponseEntity

interface WorkExperienceService {
    fun createWorkExperience(experience: AddWorkExperience, service: MstService): ResponseEntity<BaseResponse<MstWorkExperience>>
    fun searchWorkExperience(searchValue: String): ResponseEntity<BaseResponse<List<MstWorkExperience>>>
    fun getWorkExperience(id: Int): ResponseEntity<BaseResponse<MstWorkExperience>>
    fun getAllWorkExperiences(): ResponseEntity<BaseResponse<List<MstWorkExperience>>>
    fun deleteWorkExperience(workExperience: MstWorkExperience): ResponseEntity<BaseResponse<Any>>
    fun updateWorkExperience(existWorkExperience: MstWorkExperience, updateExperience: AddWorkExperience, service: MstService?): ResponseEntity<BaseResponse<MstWorkExperience>>
}