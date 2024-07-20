package com.citra.graha.dto.request

data class AddProductRequest(
    val productAddress: String,
    val productBedroom: Int,
    val propertyId: Int,
    val productDescription:String,
    val productBathroom:Int,
    val productLuasTanah:Float,
    val productLuasBangunan:Float,
    val productElectricity:Int,
    val statusId: Int,
    val productVisitCount:Int,
)
