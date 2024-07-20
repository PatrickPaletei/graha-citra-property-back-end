package com.citra.graha.repository

import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.MstPropertyType
import com.citra.graha.entity.MstStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductRepository : JpaRepository<MstProduct, Int> {
    fun findByProductId(id: Int): Optional<MstProduct>
    fun findByPropertyId(propertyId: MstPropertyType): List<MstProduct>
    fun findByStatusId(status: MstStatus): List<MstProduct>
}