package com.citra.graha.controller

import com.citra.graha.dto.request.AddRoleRequest
import com.citra.graha.dto.request.DeleteRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstRole
import com.citra.graha.repository.RoleRepository
import com.citra.graha.service.RoleService
import com.citra.graha.util.ApiDocumentation
import com.citra.graha.util.swaggerschema.BaseResponseWithListMstRole
import com.citra.graha.util.swaggerschema.BaseResponseWithMstRole
import com.citra.graha.util.swaggerschema.NullResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
    @Operation(
        summary = "Create Role",
        description = "buat nambahin role, memiliki 1 field yaitu roleName dan tidak boleh null",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "add role",
                            value = ApiDocumentation.Request.ROLE_REQUEST_EXAMPLE
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "400",
                description = "status = F. possible messages:\n " +
                        "1. roleName tidak boleh null\n " +
                        "2. role already exist",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Success add role",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstRole::class)
                    )
                ]
            )
        ]
    )
    fun addRole(@RequestBody role: AddRoleRequest): ResponseEntity<BaseResponse<MstRole>>{
        if(role.roleName == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "roleName tidak boleh null",
                    data = null
                )
            )
        }
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
    @Operation(
        summary = "Get all Role",
        description = "buat ambil semua role",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = No roles found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Get all roles",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListMstRole::class)
                    )
                ]
            )
        ]
    )
    fun getAllRoles(): ResponseEntity<BaseResponse<List<MstRole>>>{
        return roleService.getAllRoles()
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get Role by id",
        description = "buat ambil role berdasarkan ID",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Roles not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Role by id",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstRole::class)
                    )
                ]
            )
        ]
    )
    fun getRoleById(@Parameter(description = "id role yang ingin di-GET", required = true) @PathVariable id: String) : ResponseEntity<BaseResponse<MstRole>>{
        return roleService.getRoleById(Integer.valueOf(id))
    }

    @PutMapping
    @Operation(
        summary = "Update Role",
        description = "buat update role, id tidak boleh null",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "update role",
                            value = ApiDocumentation.Request.ROLE_UPDATE_REQUEST_EXAMPLE
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Role not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Role updated",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithMstRole::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "status = F. possible messages:\n " +
                        "1. Please input the role id\n " +
                        "2. Role name already exist",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
        ]
    )
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

    @DeleteMapping("/{idRole}")
    @Operation(
        summary = "Delete Role",
        description = "buat hapus role",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = Role not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Role deleted",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun deleteRole(@Parameter(description = "id role yang mau dihapus", required = true) @PathVariable idRole: Int): ResponseEntity<BaseResponse<Any>>{
        val role = roleRepository.findById(idRole)
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