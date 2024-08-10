package com.citra.graha.util

import com.citra.graha.dto.response.BaseResponse
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.nio.file.Path

object PhotoUtil {
    fun loadPhotoFromPath(uploadPath: Path, fileName: String): ResponseEntity<Any> {
        return try {
            val filePath = uploadPath.resolve(fileName).normalize()
            val resource: Resource = FileSystemResource(filePath)

            if (resource.exists() && resource.isReadable) {
                ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                        BaseResponse(
                        status = "F",
                        message = "File not found",
                        data = null
                    )
                    )
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null)
        }
    }
}