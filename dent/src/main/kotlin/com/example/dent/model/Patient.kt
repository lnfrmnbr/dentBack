package com.example.dent.model

import java.util.*

data class Patient(
    val id: UUID? = null,
    val fullName: String,
    val birthDate: Date,
    val sex: String
)