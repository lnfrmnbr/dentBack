package com.example.dent.service

import com.example.dent.model.Clinic
import com.example.dent.repository.ClinicRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClinicService(private val repo: ClinicRepository) {
    fun getAll(): List<Clinic> = repo.findAll()
    fun getById(id: UUID): Clinic? = repo.findById(id)
    fun create(patient: Clinic): Clinic = repo.save(patient)
}