package com.citra.graha.controller

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.StatusResponse
import com.citra.graha.entity.MstStatus
import com.citra.graha.repository.ProductRepository
import com.citra.graha.repository.StatusRepository
import com.citra.graha.service.StatusService
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/status")
class StatusController(
    val statusService: StatusService,
    val productRepository: ProductRepository,
    val statusRepository: StatusRepository
) {
    @PostMapping("/addStatus/{status}")
    fun addStatus(@PathVariable status: String): ResponseEntity<BaseResponse<Any>> {
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
    fun getAllStatus(): ResponseEntity<BaseResponse<List<StatusResponse>>> {
        return statusService.getAllStatus()
    }

    @DeleteMapping("/removeStatus/{idStatus}")
    fun removeStatus(@PathVariable idStatus: Int): ResponseEntity<BaseResponse<Any>> {
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
        if (existInMstProperty.isPresent) {
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