package com.citra.graha.util

import com.citra.graha.dto.response.BaseResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse

object ApiDocumentation {
    object Request {
        const val ADD_SERVICE_REQUEST_EXAMPLE = """
            {
                "serviceName": "Buat Kos",
                "serviceDescription": "Jasa Buat kos"
            }
        """
        const val ADD_PRODUCT_REQUEST_EXAMPLE = """
            {
                "productAddress": "123 Main St",
                "productBedroom": 3,
                "propertyId": 1,
                "productDescription": "A beautiful home with 3 bedrooms",
                "productBathroom": 2,
                "productLuasTanah": 120.0,
                "productLuasBangunan": 80.0,
                "productElectricity": 2200,
                "statusId": 1
            }
        """
        const val UPDATE_PRODUCT_REQUEST_EXAMPLE = """
            {
                "id": 1,
                "productAddress": "123 Main St",
                "productBedroom": 3,
                "propertyId": 1,
                "productDescription": "A beautiful home with 3 bedrooms",
                "productBathroom": 2,
                "productLuasTanah": 120.0,
                "productLuasBangunan": 80.0,
                "productElectricity": 2200,
                "statusId": 1,
                "productVisitCount": 5
            }
        """
        const val UPDATE_PRODUCT_STATUS_REQUEST_EXAMPLE = """
            {
                "id": 1,
                "statusId": 1
            }
        """
        const val UPDATE_PRODUCT_SOME_FIELDS_REQUEST_EXAMPLE = """
            {
                "id": 1,
                "productDescription": "A beautiful home with 3 bedrooms",
                "productElectricity": 2200,
                "statusId": 1,
                "productVisitCount": 5
            }
        """
        const val UPDATE_PROPERTY_TYPE_REQUEST_EXAMPLE = """
            {
                "propertyId": 1,
                "propertyName": "Test"
            }
        """
    }
}