package com.example.dent.model

import java.util.*

data class Patient(
    val id: UUID? = null,
    val clinicId: UUID?,
    val fullName: String,
    val birthDate: Date,
    val sex: String,
    val email: String? = null,
    val phoneNumber: String? = null
)