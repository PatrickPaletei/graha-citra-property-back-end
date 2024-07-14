package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name="mst_service")
data class MstService (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="service_id")
    val idService: Int ?= null,

    @Column(name="service_name")
    val serviceName: String,

    @Column(name="service_description")
    val serviceDescription: String
)