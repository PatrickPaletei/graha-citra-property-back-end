package com.citra.graha.repository

import com.citra.graha.entity.MstRole
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository: JpaRepository<MstRole, Int> {
    fun findByRoleName(roleName: String): Optional<MstRole>
}