package com.citra.graha.service

import com.citra.graha.dto.request.CreateUserRequest
import com.citra.graha.dto.request.LoginUserRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.CreateUserResponse
import com.citra.graha.dto.response.LoginResponse
import com.citra.graha.entity.MstRole
import com.citra.graha.entity.MstUser
import org.springframework.http.ResponseEntity

interface UserService {
    fun createUser(user: CreateUserRequest, role: MstRole): ResponseEntity<BaseResponse<CreateUserResponse>>
    fun getUser(userId: Int): ResponseEntity<BaseResponse<CreateUserResponse>>
    fun loginUser(user: LoginUserRequest): ResponseEntity<BaseResponse<LoginResponse>>
    fun updateUser(existUser: MstUser,updateUser: CreateUserRequest, role: MstRole?): ResponseEntity<BaseResponse<CreateUserResponse>>
    fun deleteUser(user: MstUser): ResponseEntity<BaseResponse<Any>>
    fun getAllUser(): ResponseEntity<BaseResponse<List<CreateUserResponse>>>
}