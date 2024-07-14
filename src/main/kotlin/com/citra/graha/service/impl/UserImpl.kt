package com.citra.graha.service.impl

import com.citra.graha.dto.request.CreateUserRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.CreateUserResponse
import com.citra.graha.entity.MstRole
import com.citra.graha.entity.MstUser
import com.citra.graha.repository.RoleRepository
import com.citra.graha.repository.UserRepository
import com.citra.graha.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserImpl(
    val roleRepository: RoleRepository,
    val userRepository: UserRepository
): UserService {
    override fun createUser(user: CreateUserRequest, role: MstRole): ResponseEntity<BaseResponse<CreateUserResponse>> {
        val user = MstUser(
            username = user.username,
            email = user.email,
            password = user.password,
            idRole = role,
        )

        userRepository.save(user)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "User created",
                data = CreateUserResponse(
                    username = user.username,
                    role = role
                )
            )
        )
    }

}