package com.citra.graha.controller

import com.citra.graha.dto.request.CreateUserRequest
import com.citra.graha.dto.request.DeleteRequest
import com.citra.graha.dto.request.LoginUserRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.CreateUserResponse
import com.citra.graha.repository.RoleRepository
import com.citra.graha.repository.UserRepository
import com.citra.graha.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth/user")
class UserController(
    val userService: UserService,
    val roleRepository: RoleRepository,
    val userRepository: UserRepository
) {
    @PostMapping("/register")
    fun createUser(@RequestBody user: CreateUserRequest): ResponseEntity<BaseResponse<CreateUserResponse>>{
        if(user.username == null || user.password == null || user.email == null || user.roleId == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Make sure all parameter is not null",
                    data = null
                )
            )
        }
        val existUsername = userRepository.findByUsername(user.username)
        if (existUsername.isPresent){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Username is already exist",
                    data = null
                )
            )
        }
        val existEmail = userRepository.findByEmail(user.email)

        if (existEmail.isPresent){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Email is already exist",
                    data = null
                )
            )
        }
        if (user.status != null && (user.status != "01" && user.status != "00")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Status not valid",
                    data = null
                )
            )
        }
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

    @PostMapping("/login")
    fun loginUser(@RequestBody user: LoginUserRequest): ResponseEntity<BaseResponse<CreateUserResponse>>{
        if(user.username == null || user.password == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Make sure all parameter is not null",
                    data = null
                )
            )
        }
        return userService.loginUser(user)
    }

    @PutMapping
    fun updateUser(@RequestBody user: CreateUserRequest): ResponseEntity<BaseResponse<CreateUserResponse>>{
        if (user.id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Please input the user id",
                    data = null
                )
            )
        }
        val existUser = userRepository.findById(Integer.valueOf(user.id))
        if (existUser.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "User not found",
                    data = null
                )
            )
        }
        if (user.username != null && existUser.get().username != user.username){
            val existUsername = userRepository.findByUsername(user.username)
            if (existUsername.isPresent){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    BaseResponse(
                        status = "F",
                        message = "Username is already exist",
                        data = null
                    )
                )
            }
        }

        if(user.email != null && existUser.get().email != user.email){
            val existEmail = userRepository.findByEmail(user.email)
            if (existEmail.isPresent){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    BaseResponse(
                        status = "F",
                        message = "Email is already exist",
                        data = null
                    )
                )
            }
        }
        if (user.status != null && (user.status != "01" && user.status != "00")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse(
                    status = "F",
                    message = "Status not valid",
                    data = null
                )
            )
        }
        if (user.roleId != null){
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
            return userService.updateUser(existUser = existUser.get(), updateUser = user, existsRole.get())
        }
        return userService.updateUser(existUser = existUser.get(), updateUser = user, null)
    }

    @DeleteMapping
    fun deleteUser(@RequestBody deleteRequest: DeleteRequest): ResponseEntity<BaseResponse<Any>>{
        val user = userRepository.findById(deleteRequest.id)
        if(user.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "User not found",
                    data = null
                )
            )
        }
        return userService.deleteUser(user.get())
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<BaseResponse<CreateUserResponse>>{
        // pengecekan token
        return userService.getUser(Integer.valueOf(id))
    }

    @GetMapping("/allusers")
    fun getAllUsers(): ResponseEntity<BaseResponse<List<CreateUserResponse>>>{
        return userService.getAllUser()
    }
}