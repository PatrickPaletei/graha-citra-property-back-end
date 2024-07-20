package com.citra.graha.dto

import java.nio.file.Path

data class DeletePhotoDto(
    val canDelete: Boolean,
    val listPhotoId: List<Int>?,
    val listPhotoPath: List<Path>?
)
