package com.citra.graha.repository

import com.citra.graha.entity.MstService
import com.citra.graha.entity.MstWorkExperience
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface WorkExperienceRepository: JpaRepository<MstWorkExperience, Int> {
    fun findByServiceId(serviceId: MstService): List<MstWorkExperience>

    @Query("SELECT WE.* FROM mst_work_experience WE JOIN mst_service S " +
            "on WE.service_id = S.service_id " +
            "WHERE WE.work_experience_description LIKE %:searchValue% " +
            "OR WE.work_experience_name LIKE %:searchValue% " +
            "OR S.service_description LIKE %:searchValue% " +
            "OR S.service_name LIKE %:searchValue%", nativeQuery = true)
    fun searchWorkExperience(@Param("searchValue") searchValue: String): List<MstWorkExperience>
}