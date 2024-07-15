package com.citra.graha.service

import com.citra.graha.dto.request.AddRoleRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstRole
import org.springframework.http.ResponseEntity

interface RoleService {
    fun addRole(role: AddRoleRequest): ResponseEntity<BaseResponse<MstRole>>
    fun getAllRoles(): ResponseEntity<BaseResponse<List<MstRole>>>
    fun getRoleById(id: Int): ResponseEntity<BaseResponse<MstRole>>
    fun updateRole(existRole: MstRole, updateRole: AddRoleRequest): ResponseEntity<BaseResponse<MstRole>>
    fun deleteRole(role: MstRole): ResponseEntity<BaseResponse<Any>>
}