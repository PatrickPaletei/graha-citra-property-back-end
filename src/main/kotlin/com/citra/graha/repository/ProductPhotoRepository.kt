package com.citra.graha.repository

import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.ProductPhoto
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ProductPhotoRepository: JpaRepository<ProductPhoto, Int> {
    fun findByProductId(productId: MstProduct): List<ProductPhoto>

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductPhoto p WHERE p.id IN :ids")
    fun deleteByIds(@Param("ids") ids: List<Int>)
}