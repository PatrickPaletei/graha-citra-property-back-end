package com.citra.graha.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class PathMatchingInterceptor(
    val authInterceptor: AuthInterceptor
): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor).excludePathPatterns(
            "/api/auth/user/login",
            "/api/auth/user/register",
            "/swagger-ui.html",
            "/swagger-ui/**",           // Exclude Swagger UI static resources
            "/v3/api-docs/**",           // Exclude OpenAPI docs
            "/swagger-resources/**",     // Exclude Swagger resources
            "/configuration/**",         // Exclude Swagger configuration
            "/webjars/**"
        )
    }
}