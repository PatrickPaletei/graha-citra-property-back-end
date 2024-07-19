package com.citra.graha.service.impl

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.ProductPhoto
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.service.ProductPhotoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
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
import java.util.*

@Service
class ProductPhotoImpl(
    val productPhotoRepository: ProductPhotoRepository,
    @Value("\${file.upload-dir}") val uploadDir: String
): ProductPhotoService {

    private val uploadPath: Path = Paths.get(uploadDir)

    init {
        Files.createDirectories(uploadPath)
    }


    override fun addPhotoOrVideo(photoOrVideo: List<MultipartFile>, idPhoto: MstProduct): ResponseEntity<BaseResponse<Any>> {
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

                val productPhoto = ProductPhoto(
                    productId = idPhoto,
                    filePath = targetLocation.toString(),
                    fileName = newFileName
                )
                productPhotoRepository.save(productPhoto)
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

    private fun generateCustomFileName(originalFileName: String): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val now = LocalDateTime.now().format(dateTimeFormatter)
        val extension = originalFileName.substringAfterLast('.', "")
        return "$now.$extension"
    }

    override fun getPhotoOrVideoByProductId(idProduct: MstProduct): ResponseEntity<BaseResponse<List<PhotoResponse>>> {
        val photos = productPhotoRepository.findByProductId(idProduct)
        if (photos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "No photo found for product id $idProduct",
                    data = null
                )
            )
        }
        val photoResponses = photos.map {
            PhotoResponse(
                productPhotoId = it.productPhotoId!!,
                fileName = it.fileName,
            )
        }
        return ResponseEntity.ok(BaseResponse("T", "Photos retrieved successfully", photoResponses))
    }


    override fun deletePhoto(idProductPhoto: Int): ResponseEntity<BaseResponse<Any>> {
        val photo = productPhotoRepository.findById(idProductPhoto)
        return try {
            val productPhoto = photo.get()
            val filePath = Paths.get(productPhoto.filePath)

            if (Files.exists(filePath)) {
                Files.delete(filePath)
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    BaseResponse(
                        status = "F",
                        message = "File not found",
                        data = null
                    )
                )
            }

            productPhotoRepository.delete(productPhoto)

            ResponseEntity.ok(
                BaseResponse(
                    status = "T",
                    message = "Photo deleted successfully",
                    data = null
                )
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                BaseResponse(
                    status = "F",
                    message = "Failed to delete photo",
                    data = null
                )
            )
        }
    }

    override fun loadPhoto(fileName: String): ResponseEntity<Any> {
        return try {
            val filePath = uploadPath.resolve(fileName).normalize()
            val resource: Resource = FileSystemResource(filePath)

            if (resource.exists() && resource.isReadable) {
                ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse(
                        status = "F",
                        message = "File not found",
                        data = null
                    ))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null)
        }
    }


}

