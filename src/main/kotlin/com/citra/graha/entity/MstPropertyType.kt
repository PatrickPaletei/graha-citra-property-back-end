package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name = "mst_property_type")
data class MstPropertyType(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="property_id")
    val propertyId: Int ?=null,

    @Column(name = "property_name")
    val propertyName: String

)
