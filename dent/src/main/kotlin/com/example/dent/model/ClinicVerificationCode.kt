package com.example.dent.model

import java.time.LocalDateTime
import java.util.*

data class ClinicVerificationCode(
    val id: UUID? = null,
    val clinicId: UUID,
    val code: String,
    val email: String,
    val expiresAt: LocalDateTime,
    val verified: Boolean
)