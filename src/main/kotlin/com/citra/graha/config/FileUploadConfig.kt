package com.citra.graha.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FileUploadConfig(
    @Value("\${file.upload-dir}") val uploadDir: String
)