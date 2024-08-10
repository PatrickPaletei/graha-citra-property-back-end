package com.citra.graha.repository

import com.citra.graha.entity.MstWorkExperience
import com.citra.graha.entity.WorkExpPhoto
import org.springframework.data.jpa.repository.JpaRepository

interface WorkExpPhotoRepository: JpaRepository<WorkExpPhoto, Int> {
    fun findByWorkExperienceId(workExperienceId : MstWorkExperience): List<WorkExpPhoto>
}