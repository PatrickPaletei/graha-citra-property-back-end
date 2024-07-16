package com.citra.graha.repository

import com.citra.graha.entity.MstPropertyType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PropertyTypeRepository: JpaRepository<MstPropertyType, Int> {
    fun findByPropertyNameIgnoreCase(propertyName: String): Optional<MstPropertyType>
}