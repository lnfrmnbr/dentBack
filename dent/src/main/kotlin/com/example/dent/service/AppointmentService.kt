package com.example.dent.service

import com.example.dent.model.Appointment
import com.example.dent.repository.AppointmentRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppointmentService(private val repo: AppointmentRepository) {
    fun getAll(): List<Appointment> = repo.findAll()
    fun getById(id: UUID): Appointment? = repo.findById(id)
    fun create(app: Appointment): Appointment = repo.save(app)
}
