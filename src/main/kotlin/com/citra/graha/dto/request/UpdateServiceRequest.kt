package com.citra.graha.dto.request

data class UpdateServiceRequest(
    val id: Int? = null,
    val serviceName: String? = null,
    val serviceDescription: String? = null,
)
