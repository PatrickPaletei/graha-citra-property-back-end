package com.citra.graha.service.impl

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.StatusResponse
import com.citra.graha.entity.MstStatus
import com.citra.graha.repository.StatusRepository
import com.citra.graha.service.StatusService
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class StatusImpl(
    val statusRepository: StatusRepository,
):StatusService {
    override fun addStatus(status: String): ResponseEntity<BaseResponse<Any>> {
        val newStatus = MstStatus(
            statusName = status
        )
        statusRepository.save(newStatus)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Status $status added successfully"
            )
        )
    }

    override fun getAllStatus(): ResponseEntity<BaseResponse<List<StatusResponse>>> {
        val allStatus = statusRepository.findAll()
        if (allStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "No Status found",
                    data = null
                )
            )
        }
        val listStatus = mutableListOf<StatusResponse>()
        allStatus.forEach {
            listStatus.add(
                StatusResponse(
                    statusId = it.statusId!!,
                    statusName = it.statusName
                )
            )
        }
        return ResponseEntity.ok().body(
            BaseResponse(
                status = "T",
                message = "Get All Status success",
                data = listStatus
            )
        )
    }

    override fun deleteStatus(idStatus: Int): ResponseEntity<BaseResponse<Any>> {
        val status = statusRepository.findById(idStatus)
        statusRepository.delete(status.get())
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Status $idStatus removed successfully",
                data = null
            )
        )
    }
}