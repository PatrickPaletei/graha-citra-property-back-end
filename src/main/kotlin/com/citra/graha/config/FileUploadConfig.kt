package com.citra.graha.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FileUploadConfig(
    @Value("\${file.upload-dir-product-photo}") val uploadDir: String,
    @Value("\${file.upload-dir-work-exp}") val uploadDirWorkExp: String
)