package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name="mst_role")
data class MstRole(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    val roleId: Int,

    @Column(name="role_name")
    val roleName: String
)
