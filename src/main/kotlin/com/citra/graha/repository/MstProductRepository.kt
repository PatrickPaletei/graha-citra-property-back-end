package com.citra.graha.repository

import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.ProductPhoto
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MstProductRepository : JpaRepository<MstProduct, Int> {
    fun findByProductId(id: Int): Optional<MstProduct>
}