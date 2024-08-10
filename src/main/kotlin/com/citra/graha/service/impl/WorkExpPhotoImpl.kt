package com.citra.graha.service.impl

import com.citra.graha.config.FileUploadConfig
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.WorkExpPhotoResponse
import com.citra.graha.entity.MstWorkExperience
import com.citra.graha.entity.ProductPhoto
import com.citra.graha.entity.WorkExpPhoto
import com.citra.graha.repository.WorkExpPhotoRepository
import com.citra.graha.service.WorkExpPhotoService
import com.citra.graha.util.PhotoUtil
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class WorkExpPhotoImpl(
    val workExpPhotoRepository: WorkExpPhotoRepository,
    fileUploadConfig: FileUploadConfig
) : WorkExpPhotoService {

    private val uploadPath: Path = Paths.get(fileUploadConfig.uploadDirWorkExp)

    init {
        Files.createDirectories(uploadPath)
    }

    override fun addPhotoOrVideo(
        photoOrVideo: List<MultipartFile>,
        idWorkExp: MstWorkExperience
    ): ResponseEntity<BaseResponse<Any>> {
        try {
            if (photoOrVideo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    BaseResponse(
                        status = "F",
                        message = "Please select at least one file to upload",
                        data = null
                    )
                )
            }
            photoOrVideo.forEach { photo ->
                val originalFileName = photo.originalFilename ?: throw IllegalArgumentException("Invalid file name")
                val newFileName = generateCustomFileName(originalFileName)
                val targetLocation = uploadPath.resolve(newFileName)
                Files.write(targetLocation, photo.bytes)

                val workExpPhoto = WorkExpPhoto(
                    workExperienceId = idWorkExp,
                    filePath = targetLocation.toString(),
                    fileName = newFileName
                )
                workExpPhotoRepository.save(workExpPhoto)
            }

            return ResponseEntity.ok(
                BaseResponse(
                    status = "T",
                    message = "Photos or video added successfully",
                    data = null
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                BaseResponse(
                    status = "F",
                    message = "Failed to upload photos or video",
                    data = null
                )
            )
        }
    }

    override fun getPhotoOrVideoByProductId(idWorkExp: MstWorkExperience): ResponseEntity<BaseResponse<List<WorkExpPhotoResponse>>> {
        val photos = workExpPhotoRepository.findByWorkExperienceId(idWorkExp)
        if (photos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "No photo found for product id $idWorkExp",
                    data = null
                )
            )
        }
        val photoResponse = photos.map {
            WorkExpPhotoResponse(
                workExpPhotoId = it.workExpPhotoId!!,
                fileName = it.fileName,
            )
        }
        return ResponseEntity.ok(BaseResponse("T", "Photos retrieved successfully", photoResponse))

    }

    override fun loadPhoto(fileName: String): ResponseEntity<Any> {
        return PhotoUtil.loadPhotoFromPath(uploadPath, fileName)
    }

    private fun generateCustomFileName(originalFileName: String): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val now = LocalDateTime.now().format(dateTimeFormatter)
        val extension = originalFileName.substringAfterLast('.', "")
        return "$now.$extension"
    }

}