package com.citra.graha.repository

import com.citra.graha.entity.MstService
import com.citra.graha.entity.MstWorkExperience
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface WorkExperienceRepository: JpaRepository<MstWorkExperience, Int> {
    fun findByServiceId(serviceId: MstService): List<MstWorkExperience>

    @Query("SELECT WE.* FROM mst_work_experience WE JOIN mst_service S " +
            "ON WE.service_id = S.service_id " +
            "WHERE LOWER(WE.work_experience_description) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(WE.work_experience_name) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(S.service_description) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(S.service_name) LIKE LOWER(CONCAT('%', :searchValue, '%'))", nativeQuery = true)
    fun searchWorkExperience(@Param("searchValue") searchValue: String): List<MstWorkExperience>
}