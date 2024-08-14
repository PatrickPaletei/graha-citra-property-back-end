package com.citra.graha.service.impl

import com.citra.graha.dto.request.CreateUserRequest
import com.citra.graha.dto.request.LoginUserRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.CreateUserResponse
import com.citra.graha.dto.response.LoginResponse
import com.citra.graha.entity.MstRole
import com.citra.graha.entity.MstUser
import com.citra.graha.repository.UserRepository
import com.citra.graha.service.UserService
import com.citra.graha.util.JWTGenerator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserImpl(
    val userRepository: UserRepository
): UserService {
    override fun createUser(user: CreateUserRequest, role: MstRole): ResponseEntity<BaseResponse<CreateUserResponse>> {
        val user = MstUser(
            username = user.username.toString(),
            email = user.email.toString(),
            password = user.password.toString(),
            status = user.status.toString(),
            idRole = role,
        )

        userRepository.save(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            BaseResponse(
                status = "T",
                message = "User created",
                data = CreateUserResponse(
                    userId = user.userId!!,
                    username = user.username,
                    role = role,
                    status = user.status
                )
            )
        )
    }

    override fun getUser(userId: Int): ResponseEntity<BaseResponse<CreateUserResponse>> {
        val user = userRepository.findById(userId)
        if(user.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "User not found",
                    data = null
                )
            )
        }

        return ResponseEntity.ok().body(
            BaseResponse(
                status = "T",
                message = "User found",
                data = CreateUserResponse(
                    userId = user.get().userId!!,
                    username = user.get().username,
                    role = user.get().idRole,
                    status = user.get().status
                )
            )
        )
    }

    override fun loginUser(user: LoginUserRequest): ResponseEntity<BaseResponse<LoginResponse>> {
        val userExist = userRepository.findByUsername(user.username)
        if (userExist.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "User not found",
                    data = null
                )
            )
        }
        if (userExist.get().password != user.password){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                BaseResponse(
                    status = "F",
                    message = "Wrong password",
                    data = null
                )
            )
        }
        val token = JWTGenerator().createJWT(userExist.get())
        return ResponseEntity.ok().body(
            BaseResponse(
                status = "T",
                message = "User logged in",
                data = LoginResponse(
                    userId = userExist.get().userId!!,
                    username = userExist.get().username,
                    role = userExist.get().idRole,
                    status = userExist.get().status,
                    token = token
                )
            )
        )
    }

    override fun updateUser(
        user: MstUser,
        updateUser: CreateUserRequest,
        role: MstRole?
    ): ResponseEntity<BaseResponse<CreateUserResponse>> {
        val updatedUser = user.copy(
            username = updateUser.username ?: user.username,
            email = updateUser.email ?: user.email,
            password = updateUser.password ?: user.password,
            status = updateUser.status ?: user.status,
            idRole = role ?: user.idRole
        )
        userRepository.save(updatedUser)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "User updated",
                data = CreateUserResponse(
                    userId = updatedUser.userId!!,
                    username = updatedUser.username,
                    role = updatedUser.idRole,
                    status = updatedUser.status
                )
            )
        )
    }

    override fun deleteUser(user: MstUser): ResponseEntity<BaseResponse<Any>> {
        val updatedUser = user.copy(
            status = "00"
        )
        userRepository.save(updatedUser)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "User deleted",
                data = null
            )
        )
    }

    override fun getAllUser(): ResponseEntity<BaseResponse<List<CreateUserResponse>>> {
        val allUsers = userRepository.findAll()
        if(allUsers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "No users found",
                    data = null
                )
            )
        }
        val listUsers = mutableListOf<CreateUserResponse>()
        allUsers.forEach{
            listUsers.add(
                CreateUserResponse(
                    userId = it.userId!!,
                    username = it.username,
                    role = it.idRole,
                    status = it.status
                )
            )
        }
        return ResponseEntity.ok().body(
            BaseResponse(
                status = "T",
                message = "Get all user",
                data = listUsers
            )
        )
    }

}