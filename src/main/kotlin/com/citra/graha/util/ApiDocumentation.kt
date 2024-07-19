package com.citra.graha.util

import com.citra.graha.dto.response.BaseResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse

object ApiDocumentation {
    const val ADD_SERVICE_REQUEST_EXAMPLE = """
        {
            "serviceName": "Buat Kos",
            "serviceDescription": "Jasa Buat kos"
        }
    """

}