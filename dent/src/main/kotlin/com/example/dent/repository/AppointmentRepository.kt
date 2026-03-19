package com.example.dent.repository

import com.example.dent.model.Appointment
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
class AppointmentRepository(private val jdbc: JdbcTemplate) {

    private val mapper: RowMapper<Appointment> = RowMapper { rs, _ ->
        Appointment(
            id = rs.getObject("id", UUID::class.java),
            doctorId = rs.getObject("doctor_id", UUID::class.java),
            patientId = rs.getObject("patient_id", UUID::class.java),
            date = rs.getTimestamp("date").toLocalDateTime(),
            tags = parseTags(rs.getString("tags")),
            bop = rs.getDouble("bop").takeIf { !rs.wasNull() },
            russel = rs.getDouble("russel").takeIf { !rs.wasNull() },
            api = rs.getDouble("api").takeIf { !rs.wasNull() },
            chart = rs.getString("chart"),
            fileUrl = rs.getString("file_url")
        )
    }

    fun findAllForDoctor(doctorId: UUID): List<Appointment> {
        val sql = """
        SELECT a.id AS appointment_id, 
               a.doctor_id, 
               a.patient_id, 
               p.full_name AS patient_full_name, 
               a.date, a.tags
        FROM appointments a
        JOIN patients p ON a.patient_id = p.id
        WHERE a.doctor_id = ?
    """

        return jdbc.query(sql, { rs, _ ->
            Appointment(
                id = rs.getObject("appointment_id", UUID::class.java),
                doctorId = rs.getObject("doctor_id", UUID::class.java),
                patientId = rs.getObject("patient_id", UUID::class.java),
                patientFullName = rs.getString("patient_full_name"),
                date = rs.getTimestamp("date").toLocalDateTime(),
                tags = parseTags(rs.getString("tags"))
            )
        }, doctorId)
    }


    fun findById(id: UUID): Appointment? =
        jdbc.query("SELECT * FROM appointments WHERE id = ?", mapper, id).firstOrNull()

    fun save(app: Appointment): Appointment {
        val id = jdbc.queryForObject(
            "INSERT INTO appointments(doctor_id, patient_id, date, tags, bop, russel, api, chart, file_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id",
            UUID::class.java,
            app.doctorId,
            app.patientId,
            Timestamp.valueOf(app.date),
            app.tags.joinToString(","),
            app.bop,
            app.russel,
            app.api,
            app.chart,
            app.fileUrl
        )
        return app.copy(id = id)
    }

    private fun parseTags(raw: String?): List<String> {
        return if (raw.isNullOrBlank()) {
            emptyList()
        } else {
            raw
                .removePrefix("{")
                .removeSuffix("}")
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
    }
}
