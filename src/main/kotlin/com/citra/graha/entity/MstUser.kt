package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name = "mst_user")
data class MstUser(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    val userId: Int? = null,

    @Column(name = "username")
    val username: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "password")
    val password: String,

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, foreignKey = ForeignKey(name="FK_ID_ROLE"))
    val idRole: MstRole
)
