package com.example.dent.repository

import com.example.dent.model.Clinic
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ClinicRepository(private val jdbc: JdbcTemplate) {

    private val mapper: RowMapper<Clinic> = RowMapper { rs, _ ->
        Clinic(
            id = rs.getObject("id", UUID::class.java),
            logo = rs.getString("logo"),
            orgName = rs.getString("org_name"),
            shortName = rs.getString("short_name"),
            contacts = rs.getString("contacts"),
            address = rs.getString("address"),
            email = rs.getString("email")
        )
    }

    fun findAll(): List<Clinic> = jdbc.query("SELECT * FROM clinics", mapper)

    fun findById(id: UUID): Clinic? =
        jdbc.query("SELECT * FROM clinics WHERE id = ?", mapper, id).firstOrNull()

    fun save(clinic: Clinic): Clinic {
        val id = jdbc.queryForObject(
            "INSERT INTO clinics(logo, org_name, short_name, contacts, address, email) VALUES (?, ?, ?, ?, ?, ?) RETURNING id",
            UUID::class.java,
            clinic.logo,
            clinic.orgName,
            clinic.shortName,
            clinic.contacts,
            clinic.address,
            clinic.email
        )
        return clinic.copy(id = id)
    }
}