package com.citra.graha.controller

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.StatusResponse
import com.citra.graha.entity.MstStatus
import com.citra.graha.repository.ProductRepository
import com.citra.graha.repository.StatusRepository
import com.citra.graha.service.StatusService
import com.citra.graha.util.swaggerschema.BaseResponseWithListMstStatus
import com.citra.graha.util.swaggerschema.NullResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/status")
@Tag(name = "Status API", description = "API for managing status")
class StatusController(
    val statusService: StatusService,
    val productRepository: ProductRepository,
    val statusRepository: StatusRepository
) {
    @PostMapping("/addStatus/{status}")
    @Operation(
        summary = "Add Status",
        description = "buat status baru",
        responses = [
            ApiResponse(
                responseCode = "409",
                description = "status = F. message = Status with name <status> already exists",
                content = [
                    Content(
                        mediaType = "applicatoin/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Status <status> added successfully",
                content = [
                    Content(
                        mediaType = "applicatoin/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun addStatus(@Parameter(description = "status baru yang mau ditambahkan") @PathVariable status: String): ResponseEntity<BaseResponse<Any>> {
        val existStatus = statusRepository.findByStatusNameIgnoreCase(status)
        if (existStatus.isPresent) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                BaseResponse(
                    status = "F",
                    message = "Status with name ${existStatus.get()} already exists",
                    data = null
                )
            )
        }
        return statusService.addStatus(status)
    }

    @GetMapping("/getAllStatus")
    @Operation(
        summary = "Get all Status",
        description = "mendapatkan semua status",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = No Status found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Get All Status success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListMstStatus::class)
                    )
                ]
            )
        ]
    )
    fun getAllStatus(): ResponseEntity<BaseResponse<List<MstStatus>>> {
        return statusService.getAllStatus()
    }

    @DeleteMapping("/removeStatus/{idStatus}")
    @Operation(
        summary = "Delete Status",
        description = "buat hapus status",
        responses = [
            ApiResponse(
                responseCode = "403",
                description = "status = F. message = Property Type with id <idStatus> is used",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Status with id <idStatus> not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Status <idStatus> removed successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun removeStatus(@Parameter(description = "id status yang ingin dihapus", required = true) @PathVariable idStatus: Int): ResponseEntity<BaseResponse<Any>> {
        val mstStatus = MstStatus(
            statusId = idStatus,
            statusName = ""
        )
        val existInMstProperty = productRepository.findByStatusId(mstStatus)
        val status = statusRepository.findById(idStatus)
        if (status.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Status with id $idStatus not found",
                    data = null
                )
            )
        }
        if (existInMstProperty.isNotEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                BaseResponse(
                    status = "F",
                    message = "Property Type with id $idStatus is used",
                    data = null
                )
            )
        }
        return statusService.deleteStatus(status.get().statusId!!)
    }
}