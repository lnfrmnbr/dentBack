package com.example.dent.model

import java.time.LocalDateTime
import java.util.*

data class Appointment(
    val id: UUID? = null,
    val doctorId: UUID,
    val patientId: UUID,
    val patientFullName: String? = null,
    val date: LocalDateTime = LocalDateTime.now(),
    val tags: List<String>? = null,
    val bop: Double? = null,
    val russel: Double? = null,
    val api: Double? = null,
    val chart: String? = null,
    val fileUrl: String? = null
)