package com.citra.graha.util.swaggerschema

import io.swagger.v3.oas.annotations.media.Schema

@Schema(example = """
{
  "status": "string",
  "message": "string",
  "data": null
}
""")
data class NullResponse(
    val status: String,
    val message: String,
    val data: String? = null
)
