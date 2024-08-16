package com.citra.graha.service

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.StatusResponse
import com.citra.graha.entity.MstStatus
import org.springframework.http.ResponseEntity

interface StatusService {
    fun addStatus(status:String):ResponseEntity<BaseResponse<Any>>
    fun getAllStatus():ResponseEntity<BaseResponse<List<MstStatus>>>
    fun deleteStatus(idStatus:Int):ResponseEntity<BaseResponse<Any>>
}