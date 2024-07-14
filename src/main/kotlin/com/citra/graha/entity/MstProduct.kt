package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name="mst_product")
data class MstProduct (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_id")
    val id:Int,

    @Column(name="property_id")
    val propertyId:Int,

    @Column(name="product_address")
    val productAddress:String,

    @Column(name="product_description")
    val productDescription:String,

    @Column(name="product_bedroom")
    val productBedroom:Int,

    @Column(name="product_bathroom")
    val productBathroom:Int,

    @Column(name="product_luas_tanah")
    val productLuasTanah:Float,

    @Column(name="product_luas_bangunan")
    val productLuasBangunan:Float,

    @Column(name="product_electricity")
    val productElectricity:Int,

    @Column(name="status_id")
    val statusId:Int,

    @Column(name="product_visit_count")
    val productVisitCount:Int,
)