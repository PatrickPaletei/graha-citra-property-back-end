package com.citra.graha.dto.request

data class UpdateProductRequest(
    val id: Int? = null,
    val productAddress: String? = null,
    val productBedroom: Int? = null,
    val propertyId: Int? = null,
    val productDescription:String? = null,
    val productBathroom:Int? = null,
    val productLuasTanah:Float? = null,
    val productLuasBangunan:Float? = null,
    val productElectricity:Int? = null,
    val statusId: Int? = null,
    val productVisitCount:Int? = null,
)
