package com.citra.graha.repository

import com.citra.graha.entity.MstService
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ServiceRepository: JpaRepository<MstService, Int> {
    fun findByServiceName(serviceName: String): Optional<MstService>
}