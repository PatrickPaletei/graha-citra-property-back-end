package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name="mst_product")
data class MstProduct (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_id")
    val productId:Int,

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false, foreignKey = ForeignKey(name="FK_ID_PROPERTY"))
    val propertyId:MstPropertyType,

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

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false, foreignKey = ForeignKey(name="FK_ID_STATUS"))
    val statusId:MstStatus,

    @Column(name="product_visit_count")
    val productVisitCount:Int,
)