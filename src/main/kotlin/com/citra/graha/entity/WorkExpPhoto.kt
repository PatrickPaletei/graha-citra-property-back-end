package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name = "work_exp_photo")
data class WorkExpPhoto(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "work_exp_photo_id", nullable = false)
    val workExpPhotoId:Int?=null,

    @ManyToOne
    @JoinColumn(name = "work_experience_id",nullable = false, foreignKey = ForeignKey(name = "FK_work_exp_experience"))
    val workExperienceId:MstWorkExperience,

    @Column(name="file_path")
    val filePath: String,

    @Column(name="file_name")
    val fileName: String
)
