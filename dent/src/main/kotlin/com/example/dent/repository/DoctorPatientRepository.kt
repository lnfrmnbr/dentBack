package com.example.dent.repository

import com.example.dent.model.DoctorPatient
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DoctorPatientRepository(private val jdbc: JdbcTemplate) {

    fun save(dp: DoctorPatient) {
        jdbc.update(
            "INSERT INTO doctor_patient(doctor_id, patient_id) VALUES (?, ?)",
            dp.doctorId,
            dp.patientId
        )
    }

    fun findByDoctorId(doctorId: UUID): List<DoctorPatient> =
        jdbc.query(
            "SELECT * FROM doctor_patient WHERE doctor_id = ?",
            { rs, _ -> DoctorPatient(rs.getObject("doctor_id", UUID::class.java), rs.getObject("patient_id", UUID::class.java)) },
            doctorId
        )
}
