package com.citra.graha.service.impl

import com.citra.graha.config.FileUploadConfig
import com.citra.graha.dto.DeletePhotoDto
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.DeleteResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.ProductPhoto
import com.citra.graha.repository.ProductPhotoRepository
import com.citra.graha.service.ProductPhotoService
import com.citra.graha.util.PhotoUtil
import org.springframework.beans.factory.annotation.Value
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
class ProductPhotoImpl(
    val productPhotoRepository: ProductPhotoRepository,
    fileUploadConfig: FileUploadConfig
): ProductPhotoService {

    private val uploadPath: Path = Paths.get(fileUploadConfig.uploadDir)

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

    override fun checkPhotosExist(productList: List<ProductPhoto>): DeletePhotoDto{
        val listIds = mutableListOf<Int>()
        val listPath = mutableListOf<Path>()
        productList.forEach { photo ->
            val photoExist = productPhotoRepository.findById(photo.productPhotoId!!)
            val filePath = Paths.get(photoExist.get().filePath)
            listIds.add(photo.productPhotoId)
            listPath.add(filePath)
            if (photoExist.isEmpty && !Files.exists(filePath)){
                return DeletePhotoDto(
                    canDelete = false,
                    listPhotoId = null,
                    listPhotoPath = null
                )
            }
        }
        return DeletePhotoDto(
            canDelete = true,
            listPhotoId = listIds,
            listPhotoPath = listPath
        )
    }

    override fun deletePhoto(idProductPhoto: Int): DeleteResponse {
        val photo = productPhotoRepository.findById(idProductPhoto)
        return try {
            val productPhoto = photo.get()
            val filePath = Paths.get(productPhoto.filePath)

            if (Files.exists(filePath)) {
                Files.delete(filePath)
            } else {
                DeleteResponse(
                    statusCode = HttpStatus.NOT_FOUND,
                    statusString = "F",
                    message = "File not found"
                )
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    BaseResponse(
//                        status = "F",
//                        message = "File not found",
//                        data = null
//                    )
//                )
            }
            productPhotoRepository.delete(productPhoto)
            DeleteResponse(
                statusCode = HttpStatus.OK,
                statusString = "T",
                message = "Photo deleted successfully"
            )

//            ResponseEntity.ok(
//                BaseResponse(
//                    status = "T",
//                    message = "Photo deleted successfully",
//                    data = null
//                )
//            )
        } catch (e: Exception) {
            DeleteResponse(
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
                statusString = "F",
                message = "Failed to delete photo"
            )
//            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                BaseResponse(
//                    status = "F",
//                    message = "Failed to delete photo",
//                    data = null
//                )
//            )
        }
    }

    override fun loadPhoto(fileName: String): ResponseEntity<Any> {
        return PhotoUtil.loadPhotoFromPath(uploadPath, fileName)
    }

}

