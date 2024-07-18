package com.citra.graha.controller

import com.citra.graha.dto.request.AddRoleRequest
import com.citra.graha.dto.request.DeleteRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstRole
import com.citra.graha.repository.RoleRepository
import com.citra.graha.service.RoleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/role")
@Tag(name = "Role API", description = "API for managing role")
class RoleController(
    val roleService: RoleService,
    val roleRepository: RoleRepository
) {

    @PostMapping
    @Operation(summary = "Create Role", description = "buat nambahin role")
    fun addRole(@RequestBody role: AddRoleRequest): ResponseEntity<BaseResponse<MstRole>>{
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
        return roleService.addRole(role)
    }

    @GetMapping("/allroles")
    @Operation(summary = "Get all Role", description = "buat ambil semua role")
    fun getAllRoles(): ResponseEntity<BaseResponse<List<MstRole>>>{
        return roleService.getAllRoles()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Role by id", description = "buat ambil role berdasarkan ID")
    fun getRoleById(@PathVariable id: String) : ResponseEntity<BaseResponse<MstRole>>{
        return roleService.getRoleById(Integer.valueOf(id))
    }

    @PutMapping
    @Operation(summary = "Update Role", description = "buat update role")
    fun updateRole(@RequestBody updateRole: AddRoleRequest): ResponseEntity<BaseResponse<MstRole>>{
        if (updateRole.id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Please input the role id",
                    data = null
                )
            )
        }
        val existRoleName = roleRepository.findByRoleName(updateRole.roleName)
        if(existRoleName.isPresent){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Role name already exist",
                    data = null
                )
            )
        }
        val existRole = roleRepository.findById(Integer.valueOf(updateRole.id))
        if(existRole.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Role not found",
                    data = null
                )
            )
        }
        return roleService.updateRole(existRole.get(), updateRole)
    }

    @DeleteMapping
    @Operation(summary = "Delete Role", description = "buat hapus role")
    fun deleteRole(@RequestBody deleteRequest: DeleteRequest): ResponseEntity<BaseResponse<Any>>{
        val role = roleRepository.findById(deleteRequest.id)
        if(role.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Role not found",
                    data = null
                )
            )
        }
        return roleService.deleteRole(role.get())
    }
}