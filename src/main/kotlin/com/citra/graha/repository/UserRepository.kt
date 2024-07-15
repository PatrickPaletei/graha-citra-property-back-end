package com.citra.graha.repository

import com.citra.graha.entity.MstUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<MstUser, Int> {
    fun findByUsernameAndPassword(username: String, password: String): Optional<MstUser>
}