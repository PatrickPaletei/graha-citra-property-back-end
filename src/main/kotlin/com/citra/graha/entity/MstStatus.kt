package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table
data class MstStatus(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="status_id")
    val statusId: Int,

    @Column(name="status_name")
    val statusName: String
)
