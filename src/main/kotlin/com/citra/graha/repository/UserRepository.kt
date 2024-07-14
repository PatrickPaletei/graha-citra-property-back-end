package com.citra.graha.repository

import com.citra.graha.entity.MstUser
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<MstUser, Int> {
}