package com.citra.graha.repository

import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.MstPropertyType
import com.citra.graha.entity.MstStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ProductRepository : JpaRepository<MstProduct, Int> {
    fun findByProductId(id: Int): Optional<MstProduct>
    fun findByPropertyId(propertyId: MstPropertyType): List<MstProduct>
    fun findByStatusId(status: MstStatus): List<MstProduct>

    @Query("SELECT MP.* FROM mst_product MP join mst_property_type MPT " +
            "ON MP.property_id = MPT.property_id " +
            "WHERE LOWER(MP.product_address) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(MP.product_description) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(MPT.property_name)  LIKE LOWER(CONCAT('%', :searchValue, '%'))", nativeQuery = true)
    fun searchProduct(@Param("searchValue") searchValue: String): List<MstProduct>
}