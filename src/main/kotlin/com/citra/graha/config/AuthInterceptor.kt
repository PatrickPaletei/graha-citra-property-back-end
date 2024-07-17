package com.citra.graha.config

import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.util.JWTGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor: HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = request.getHeader("token")

        if(token == null){
            val body: BaseResponse<String> = BaseResponse(
                status = "403",
                message = "Unauthorized",
                data = null
            )
            internalServerError(body, response)
            return false
        }

        try{
            JWTGenerator().decodeJWT(token).id ?: throw RuntimeException("invalid token")
        }catch( e: ExpiredJwtException){
            e.printStackTrace()
            val body: BaseResponse<String> = BaseResponse("403", "Invalid token", null)
            internalServerError(body, response)
            return false
        }

        return super.preHandle(request, response, handler)
    }

    fun internalServerError(
        body: BaseResponse<String>,
        response: HttpServletResponse
    ): HttpServletResponse{
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = "application/json"
        response.writer.write(convertObjectToJson(body))
        return response
    }

    fun convertObjectToJson(dto: BaseResponse<String>): String{
        return ObjectMapper().writeValueAsString(dto)
    }

}