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
            fullName = rs.getString("full_name"),
            firstName = rs.getString("first_name"),
            sex = rs.getString("sex")
        )
    }

    fun findAll(): List<Doctor> =
        jdbc.query("SELECT * FROM doctors", mapper)

    fun findById(id: UUID): Doctor? =
        jdbc.query("SELECT * FROM doctors WHERE id = ?", mapper, id).firstOrNull()

    fun save(doctor: Doctor): Doctor {
        val id = jdbc.queryForObject(
            "INSERT INTO doctors(full_name, first_name, sex) VALUES (?, ?, ?) RETURNING id",
            UUID::class.java,
            doctor.fullName,
            doctor.firstName,
            doctor.sex
        )
        return doctor.copy(id = id)
    }
}