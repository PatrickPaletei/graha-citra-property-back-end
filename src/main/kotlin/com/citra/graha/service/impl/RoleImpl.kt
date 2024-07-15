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
    override fun addRole(role: AddRoleRequest): ResponseEntity<BaseResponse<MstRole>> {
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

    override fun getAllRoles(): ResponseEntity<BaseResponse<List<MstRole>>> {
        val rolesList = roleRepository.findAll()
        if (rolesList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "No roles found",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get all roles",
                data = rolesList
            )
        )
    }

    override fun getRoleById(id: Int): ResponseEntity<BaseResponse<MstRole>> {
        val role = roleRepository.findById(id)
        if (role.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Roles not found",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Role by id",
                data = role.get()
            )
        )
    }

    override fun updateRole(existRole: MstRole, updateRole: AddRoleRequest): ResponseEntity<BaseResponse<MstRole>> {
        val updatedRole = existRole.copy(
            roleName = updateRole.roleName
        )
        roleRepository.save(updatedRole)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Role updated",
                data = updatedRole
            )
        )
    }

    override fun deleteRole(role: MstRole): ResponseEntity<BaseResponse<Any>> {
        roleRepository.delete(role)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Role deleted",
                data = null
            )
        )
    }

}