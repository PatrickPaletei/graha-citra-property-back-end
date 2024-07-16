package com.citra.graha.repository

import com.citra.graha.entity.MstStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StatusRepository: JpaRepository<MstStatus, Int> {
    fun findByStatusNameIgnoreCase(statusName: String): Optional<MstStatus>
}