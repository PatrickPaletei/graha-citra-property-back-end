package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name = "product_photo")
data class ProductPhoto(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_photo_id")
    val productPhotoId: Int?=null,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, foreignKey = ForeignKey(name="FK_ID_PRODUCT"))
    val productId: MstProduct,

    @Column(name="file_path")
    val filePath: String,

    @Column(name="file_name")
    val fileName: String
)
