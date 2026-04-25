package com.example.dent.repository

import com.example.dent.model.Doctor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DoctorRepository(private val jdbc: JdbcTemplate) {

    private val mapper: RowMapper<Doctor> = RowMapper { rs, _ ->
        Doctor(
            id = rs.getObject("id", UUID::class.java),
            clinicId = rs.getObject("clinic_id", UUID::class.java),
            lastName = rs.getString("last_name"),
            firstName = rs.getString("first_name"),
            patronymic = rs.getString("patronymic"),
            sex = rs.getString("sex"),
            role = rs.getString("role"),
            birthDate = rs.getDate("birth_date")
        )
    }

    fun findAll(): List<Doctor> =
        jdbc.query("SELECT * FROM doctors", mapper)

    fun findById(id: UUID): Doctor? =
        jdbc.query("SELECT * FROM doctors WHERE id = ?", mapper, id).firstOrNull()

    fun getByUserId(userId: UUID): Doctor? =
        jdbc.query(
            """
        SELECT d.* 
        FROM doctors d
        JOIN users u ON d.id = u.doctor_id
        WHERE u.id = ?
        """,
            mapper,
            userId
        ).firstOrNull()

    fun save(doctor: Doctor): Doctor {
        val id = jdbc.queryForObject(
            "INSERT INTO doctors(clinic_id, last_name, first_name, patronymic, sex, role, birth_date) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id",
            UUID::class.java,
            doctor.clinicId,
            doctor.lastName,
            doctor.firstName,
            doctor.patronymic,
            doctor.sex,
            doctor.role,
            doctor.birthDate
        )
        return doctor.copy(id = id)
    }
}