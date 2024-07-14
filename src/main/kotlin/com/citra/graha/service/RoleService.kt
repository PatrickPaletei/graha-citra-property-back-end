package com.citra.graha.service

import com.citra.graha.dto.request.AddRoleRequest
import com.citra.graha.dto.response.BaseResponse
import org.springframework.http.ResponseEntity

interface RoleService {
    fun addRole(role: AddRoleRequest): ResponseEntity<BaseResponse<Any>>
}