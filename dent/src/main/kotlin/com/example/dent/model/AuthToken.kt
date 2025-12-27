package com.example.dent.model

import java.time.LocalDateTime
import java.util.*

data class AuthToken(
    val id: UUID? = null,
    val userId: UUID,
    val token: String,
    val tokenType: String = "refresh",
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val revokedAt: LocalDateTime? = null
)