package com.citra.graha.controller

import com.citra.graha.dto.request.CreateUserRequest
import com.citra.graha.dto.request.LoginUserRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.CreateUserResponse
import com.citra.graha.dto.response.LoginResponse
import com.citra.graha.repository.RoleRepository
import com.citra.graha.repository.UserRepository
import com.citra.graha.service.UserService
import com.citra.graha.util.ApiDocumentation
import com.citra.graha.util.JWTGenerator
import com.citra.graha.util.swaggerschema.BaseResponseWithCreateUserResponse
import com.citra.graha.util.swaggerschema.BaseResponseWithListCreateUserResponse
import com.citra.graha.util.swaggerschema.BaseResponseWithLoginResponse
import com.citra.graha.util.swaggerschema.NullResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
    @Operation(
        summary = "Register user",
        description = "semua field kecuali id harus diisi. Status bisa null dan memiliki default value '01'. Status cuma bisa '00' atau '01'",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "Contoh request untuk register user dengan status",
                            value = ApiDocumentation.Request.REGISTER_USER_REQUEST_EXAMPLE_WITH_STATUS
                        ),
                        ExampleObject(
                            name = "Contoh request untuk register user tanpa status",
                            value = ApiDocumentation.Request.REGISTER_USER_REQUEST_EXAMPLE_WITHOUT_STATUS
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "400",
                description = "status = F. possible messages: \n " +
                        "1. Make sure all parameter is not null\n " +
                        "2. Username is already exist\n " +
                        "3. Email is already exist\n " +
                        "4. Status not valid",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
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
                responseCode = "201",
                description = "status = T. message = User created",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithCreateUserResponse::class)
                    )
                ]
            )
        ]
    )
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
    @Operation(
        summary = "untuk melakukan login dengan username dan password",
        description = "login user akan dilakukan dengan username dan password jadi semua field (username dan password) harus diisi",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "contoh request untuk login",
                            description = "semua field (username dan password) harus diisi",
                            value = ApiDocumentation.Request.LOGIN_USER_REQUEST_EXAMPLE
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "400",
                description = "status = F. message = Make sure all parameter is not null",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = User not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "status = F. message = Wrong password",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Wrong password",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithLoginResponse::class)
                    )
                ]
            )
        ]
    )
    fun loginUser(@RequestBody user: LoginUserRequest): ResponseEntity<BaseResponse<LoginResponse>>{
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
    @Operation(
        summary = "Untuk melakukan update user",
        description = "melakukan update beberapa data user menggunakan user id. User id tidak boleh null",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "Update username",
                            description = "melakukan update username untuk user id 1",
                            value = ApiDocumentation.Request.UPDATE_USER_REQUEST_USERNAME
                        ),
                        ExampleObject(
                            name = "Update password",
                            description = "melakukan update password untuk user id 1",
                            value = ApiDocumentation.Request.UPDATE_USER_REQUEST_PASSWORD
                        ),
                        ExampleObject(
                            name = "Update role dan status",
                            description = "melakukan update role dan status untuk user id 1",
                            value = ApiDocumentation.Request.UPDATE_USER_REQUEST_ROLE_STATUS
                        )
                    ]
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "400",
                description = "status = F. possible messages: \n " +
                        "1. Please input the user id\n " +
                        "2. Username is already exist\n " +
                        "3. Email is already exist\n " +
                        "4. Status not valid",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. possible messages: \n" +
                        "1. Role not found\n " +
                        "2. User not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = User updated",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithCreateUserResponse::class)
                    )
                ]
            ),
        ]
    )
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

    @DeleteMapping("/{idUser}")
    @Operation(
        summary = "Delete user",
        description = "menghapus user berdasarakan user id pada request body",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = User not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = User deleted",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            )
        ]
    )
    fun deleteUser(@Parameter(description = "id user yang akan dihapus", required = true) @PathVariable idUser: Int): ResponseEntity<BaseResponse<Any>>{
        val user = userRepository.findById(idUser)
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
    @Operation(
        summary = "get user by id",
        description = "mendapatkan data user berdasarkan id",
        responses = [
            ApiResponse(
                responseCode = "401",
                description = "status = F. message = Unauthorized",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = user not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = User found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithCreateUserResponse::class)
                    )
                ]
            )
        ]
    )
    fun getUser(@Parameter(description = "id dari user yang mau get datanya", required = true) @PathVariable id: Int,
                @Parameter(description = "token JWT untuk authorization", required = true) @RequestHeader("token") token: String): ResponseEntity<BaseResponse<CreateUserResponse>>{
        val claim = JWTGenerator().decodeJWT(token)
        if (claim["user_id"] != id){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                BaseResponse(
                    status = "F",
                    message = "Unauthorized",
                    data = null
                )
            )
        }
        return userService.getUser(id)
    }

    @GetMapping("/allusers")
    @Operation(
        summary = "get all user",
        description = "mendapatkan semua data user",
        responses = [
            ApiResponse(
                responseCode = "404",
                description = "status = F. message = No users found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = NullResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "200",
                description = "status = T. message = Get all user",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BaseResponseWithListCreateUserResponse::class)
                    )
                ]
            )
        ]
    )
    fun getAllUsers(): ResponseEntity<BaseResponse<List<CreateUserResponse>>>{
        return userService.getAllUser()
    }
}

