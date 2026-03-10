package com.example.dent.service

import com.example.dent.model.Doctor
import com.example.dent.repository.DoctorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DoctorService(private val repo: DoctorRepository) {
    fun getAll(): List<Doctor> = repo.findAll()
    fun getById(id: UUID): Doctor? = repo.findById(id)
    fun create(doctor: Doctor): Doctor = repo.save(doctor)

    fun getByUserId(userId: UUID): Doctor? = repo.getByUserId(userId)
}