package com.citra.graha.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "Date")
data class Date(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "date_id")
    val dateId: Instant,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, foreignKey = ForeignKey(name = "FK_ID_PRODUCT"))
    val productId: MstProduct,

    @Column(name = "dt_updated")
    val dtUpdated: Instant,

    @Column(name = "dt_sold")
    val dtSold: Instant,

    @Column(name = "dt_added")
    val dtAdded: Instant
)
