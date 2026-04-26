package com.example.dent.repository

import com.example.dent.model.Clinic
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

    private val mapperDetailed: RowMapper<Doctor> = RowMapper { rs, _ ->

        val clinicId = rs.getObject("clinic_id", UUID::class.java)

        Doctor(
            id = rs.getObject("doctor_id", UUID::class.java),
            clinicId = rs.getObject("clinic_id", UUID::class.java),
            lastName = rs.getString("last_name"),
            firstName = rs.getString("first_name"),
            patronymic = rs.getString("patronymic"),
            sex = rs.getString("sex"),
            role = rs.getString("role"),
            birthDate = rs.getDate("birth_date"),

            clinic = clinicId?.let {
                Clinic(
                    id = clinicId,
                    logo = rs.getString("clinic_logo"),
                    orgName = rs.getString("org_name"),
                    shortName = rs.getString("short_name"),
                    contacts = rs.getString("contacts"),
                    address = rs.getString("address"),
                    email = rs.getString("clinic_email")
                )
            }
        )
    }

    fun findAll(): List<Doctor> =
        jdbc.query("SELECT * FROM doctors", mapper)

    fun findById(id: UUID): Doctor? =
        jdbc.query("SELECT * FROM doctors WHERE id = ?", mapper, id).firstOrNull()

    fun getByUserId(userId: UUID): Doctor? =
        jdbc.query(
            """
        SELECT 
            d.id AS doctor_id,
            d.clinic_id,
            d.last_name,
            d.first_name,
            d.patronymic,
            d.sex,
            d.role,
            d.birth_date,

            c.id AS clinic_id,
            c.logo AS clinic_logo,
            c.org_name,
            c.short_name,
            c.contacts,
            c.address,
            c.email AS clinic_email

        FROM doctors d
        JOIN users u ON d.id = u.doctor_id
        LEFT JOIN clinics c ON d.clinic_id = c.id
        WHERE u.id = ?
        """,
            mapperDetailed,
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