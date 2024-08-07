package com.citra.graha.entity

import jakarta.persistence.*

@Entity
@Table(name = "mst_work_experience")
data class MstWorkExperience (
    // id, name, service id, description
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="work_experience_id")
    val workExperienceId: Int? = null,

    @Column(name="work_experience_name")
    val name: String,

    @Column(name="work_experience_description")
    val description: String,

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false, foreignKey = ForeignKey(name="FK_ID_SERVICE"))
    val serviceId: MstService
)
