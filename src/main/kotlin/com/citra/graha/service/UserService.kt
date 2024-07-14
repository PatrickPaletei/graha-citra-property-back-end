package com.citra.graha.service

import com.citra.graha.dto.request.CreateUserRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.CreateUserResponse
import com.citra.graha.entity.MstRole
import org.springframework.http.ResponseEntity

interface UserService {
    fun createUser(user: CreateUserRequest, role: MstRole): ResponseEntity<BaseResponse<CreateUserResponse>>
}