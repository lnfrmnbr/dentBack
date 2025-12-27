package com.example.dent.service

import com.example.dent.model.Patient
import com.example.dent.repository.PatientRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PatientService(private val repo: PatientRepository) {
    fun getAll(): List<Patient> = repo.findAll()
    fun getById(id: UUID): Patient? = repo.findById(id)
    fun create(patient: Patient): Patient = repo.save(patient)
}