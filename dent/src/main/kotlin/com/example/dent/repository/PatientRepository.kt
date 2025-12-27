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
            fullName = rs.getString("full_name"),
            birthDate = rs.getString("birth_date"),
            sex = rs.getString("sex")
        )
    }

    fun findAll(): List<Patient> = jdbc.query("SELECT * FROM patients", mapper)

    fun findById(id: UUID): Patient? =
        jdbc.query("SELECT * FROM patients WHERE id = ?", mapper, id).firstOrNull()

    fun save(patient: Patient): Patient {
        val id = jdbc.queryForObject(
            "INSERT INTO patients(full_name, birth_date, sex) VALUES (?, ?, ?) RETURNING id",
            UUID::class.java,
            patient.fullName,
            patient.birthDate,
            patient.sex
        )
        return patient.copy(id = id)
    }
}
