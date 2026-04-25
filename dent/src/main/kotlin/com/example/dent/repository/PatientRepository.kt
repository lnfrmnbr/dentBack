package com.example.dent.repository

import com.example.dent.model.Patient
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class PatientRepository(private val jdbc: JdbcTemplate) {

    private val mapper: RowMapper<Patient> = RowMapper { rs, _ ->
        Patient(
            id = rs.getObject("id", UUID::class.java),
            clinicId = rs.getObject("clinic_id", UUID::class.java),
            fullName = rs.getString("full_name"),
            birthDate = rs.getDate("birth_date"),
            sex = rs.getString("sex"),
            email = rs.getString("email"),
            phoneNumber = rs.getString("phone_number")
        )
    }

    fun findAll(): List<Patient> = jdbc.query("SELECT * FROM patients", mapper)

    fun findById(id: UUID): Patient? =
        jdbc.query("SELECT * FROM patients WHERE id = ?", mapper, id).firstOrNull()

    fun save(patient: Patient): Patient {
        val id = jdbc.queryForObject(
            "INSERT INTO patients(clinic_id, full_name, birth_date, sex, email, phone_number) VALUES (?, ?, ?, ?, ?, ?) RETURNING id",
            UUID::class.java,
            patient.clinicId,
            patient.fullName,
            patient.birthDate,
            patient.sex,
            patient.email,
            patient.phoneNumber
        )
        return patient.copy(id = id)
    }
}
