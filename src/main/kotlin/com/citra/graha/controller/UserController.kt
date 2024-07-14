package com.citra.graha.controller

import com.citra.graha.dto.request.CreateUserRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.CreateUserResponse
import com.citra.graha.repository.RoleRepository
import com.citra.graha.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    val userService: UserService,
    val roleRepository: RoleRepository
) {
    @PostMapping
    fun createUser(@RequestBody user: CreateUserRequest): ResponseEntity<BaseResponse<CreateUserResponse>>{
        val existsRole = roleRepository.findById(user.roleId)
        if (existsRole.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Role not found",
                    data = null
                )
            )
        }
        return userService.createUser(user, existsRole.get())
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<BaseResponse<Any>>{
        // pengecekan token
        return userService.getUser(Integer.valueOf(id))
    }
}