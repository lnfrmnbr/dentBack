package com.example.dent.model

import java.util.*

data class User(
    val id: UUID? = null,
    val email: String,
    val passwordHash: String?,
    val salt: String,
    val doctorId: UUID
)