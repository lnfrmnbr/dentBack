package com.example.dent.model

import java.util.*

data class Doctor(
    val id: UUID? = null,
    val clinicId: UUID?,
    val lastName: String,
    val firstName: String,
    val patronymic: String?,
    val sex: String?,
    val role: String,
    val birthDate: Date,
    val clinic: Clinic? = null
)