package com.example.dent.service

import com.example.dent.model.DoctorPatient
import com.example.dent.repository.DoctorPatientRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DoctorPatientService(private val repo: DoctorPatientRepository) {
    fun assign(doctorId: UUID, patientId: UUID) = repo.save(DoctorPatient(doctorId, patientId))
    fun getByDoctorId(doctorId: UUID): List<DoctorPatient> = repo.findByDoctorId(doctorId)
}
