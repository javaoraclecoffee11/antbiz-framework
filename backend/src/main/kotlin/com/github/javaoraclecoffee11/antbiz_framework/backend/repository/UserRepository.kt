package com.github.javaoraclecoffee11.antbiz_framework.backend.repository

import com.github.javaoraclecoffee11.antbiz_framework.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository: JpaRepository<User, String> {
    fun findByEmail(email: String): Optional<User>
}