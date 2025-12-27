package com.example.dent.model

import java.time.LocalDateTime
import java.util.*

data class Appointment(
    val id: UUID? = null,
    val doctorId: UUID,
    val patientId: UUID,
    val date: LocalDateTime = LocalDateTime.now(),
    val bop: Double?,
    val russel: Double?,
    val api: Double?,
    val chart: String?,   // JSONB как строка
    val fileUrl: String?
)