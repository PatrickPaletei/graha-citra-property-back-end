package com.citra.graha.repository

import com.citra.graha.entity.MstService
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceRepository: JpaRepository<MstService, Int> {

}