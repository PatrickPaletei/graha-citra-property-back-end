package com.citra.graha.service.impl

import com.citra.graha.dto.request.AddRoleRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstRole
import com.citra.graha.repository.RoleRepository
import com.citra.graha.service.RoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RoleImpl(
    val roleRepository: RoleRepository
):RoleService {
    override fun addRole(role: AddRoleRequest): ResponseEntity<BaseResponse<Any>> {
        val existRole = roleRepository.findByRoleName(role.roleName)
        if(existRole.isPresent){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "role already exist",
                    data = null
                )
            )
        }

        val role = MstRole(
            roleName = role.roleName
        )

        roleRepository.save(role)

        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Success add role",
                data = role
            )
        )
    }
}