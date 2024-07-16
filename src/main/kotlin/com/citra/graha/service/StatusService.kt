package com.citra.graha.service

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.StatusResponse
import org.springframework.http.ResponseEntity

interface StatusService {
    fun addStatus(status:String):ResponseEntity<BaseResponse<Any>>
    fun getAllStatus():ResponseEntity<BaseResponse<List<StatusResponse>>>
    fun deleteStatus(idStatus:Int):ResponseEntity<BaseResponse<Any>>
}