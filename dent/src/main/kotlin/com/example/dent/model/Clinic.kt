package com.example.dent.model

import java.util.*

data class Clinic (
    val id: UUID? = null,
    val logo: String,
    val orgName: String,
    val shortName: String,
    val contacts: String,
    val address: String,
    val email: String
)