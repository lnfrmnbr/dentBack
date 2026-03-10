package com.example.dent.model

import java.util.*

data class Doctor(
    val id: UUID? = null,
    val fullName: String,
    val firstName: String?,
    val sex: String?,
    val role: String,
    val birthDate: Date
)