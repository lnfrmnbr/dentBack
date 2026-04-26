package com.example.dent.repository

import com.example.dent.model.Appointment
import com.example.dent.model.Indexes
import com.example.dent.model.Tooth
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
class AppointmentRepository(private val jdbc: JdbcTemplate) {

    private val objectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(KotlinModule.Builder().build())

    private val mapper: RowMapper<Appointment> = RowMapper { rs, _ ->

        val indexesJson = rs.getString("indexes")
        val chartJson = rs.getString("chart")

        Appointment(
            id = rs.getObject("id", UUID::class.java),
            doctorId = rs.getObject("doctor_id", UUID::class.java),
            patientId = rs.getObject("patient_id", UUID::class.java),
            date = rs.getTimestamp("date").toLocalDateTime(),
            tags = parseList(rs.getString("tags")),
            indexes = indexesJson?.let {
                objectMapper.readValue(it, Indexes::class.java)
            } ?: Indexes(),
            chart = chartJson?.let { it: String ->
                objectMapper.readValue(it, object : TypeReference<List<Tooth>>() {})
            },
            isFirst = rs.getBoolean("is_first"),
            diagnosis = parseList(rs.getString("diagnosis")),
            comments = rs.getString("comments"),
            complaints = rs.getString("complaints"),
            plan = rs.getString("plan")
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
                tags = parseList(rs.getString("tags"))
            )
        }, doctorId)
    }

    fun findUpcomingForDoctor(doctorId: UUID): List<Appointment> {
        val sql = """
        SELECT a.id AS appointment_id, 
               a.doctor_id, 
               a.patient_id, 
               p.full_name AS patient_full_name, 
               a.date, a.tags
        FROM appointments a
        JOIN patients p ON a.patient_id = p.id
        WHERE a.doctor_id = ?
          AND a.date >= CURRENT_DATE
        ORDER BY a.date ASC
        LIMIT 10
    """

        return jdbc.query(sql, { rs, _ ->
            Appointment(
                id = rs.getObject("appointment_id", UUID::class.java),
                doctorId = rs.getObject("doctor_id", UUID::class.java),
                patientId = rs.getObject("patient_id", UUID::class.java),
                patientFullName = rs.getString("patient_full_name"),
                date = rs.getTimestamp("date").toLocalDateTime(),
                tags = parseList(rs.getString("tags"))
            )
        }, doctorId)
    }

    fun findArchiveForDoctor(doctorId: UUID): List<Appointment> {
        val sql = """
        SELECT a.id AS appointment_id, 
               a.doctor_id, 
               a.patient_id, 
               p.full_name AS patient_full_name, 
               a.date, a.tags
        FROM appointments a
        JOIN patients p ON a.patient_id = p.id
        WHERE a.doctor_id = ?
          AND a.date < CURRENT_DATE
        ORDER BY a.date DESC
        LIMIT 10
    """

        return jdbc.query(sql, { rs, _ ->
            Appointment(
                id = rs.getObject("appointment_id", UUID::class.java),
                doctorId = rs.getObject("doctor_id", UUID::class.java),
                patientId = rs.getObject("patient_id", UUID::class.java),
                patientFullName = rs.getString("patient_full_name"),
                date = rs.getTimestamp("date").toLocalDateTime(),
                tags = parseList(rs.getString("tags"))
            )
        }, doctorId)
    }

    fun findById(id: UUID): Appointment? =
        jdbc.query("SELECT * FROM appointments WHERE id = ?", mapper, id).firstOrNull()

    fun save(app: Appointment): Appointment {

        val indexesJson = objectMapper.writeValueAsString(app.indexes)
        val chartJson = objectMapper.writeValueAsString(app.chart)

        val id = jdbc.queryForObject(
            """
        INSERT INTO appointments(
            doctor_id, patient_id, date, tags, diagnosis, complaints, comments, plan, is_first, indexes, chart
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb, ?::jsonb)
        RETURNING id
        """,
            UUID::class.java,
            app.doctorId,
            app.patientId,
            Timestamp.valueOf(app.date),
            app.tags?.joinToString(","),
            app.diagnosis?.joinToString(","),
            app.complaints,
            app.comments,
            app.plan,
            app.isFirst,
            indexesJson,
            chartJson
        )

        return app.copy(id = id)
    }

    private fun parseList(raw: String?): List<String> {
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
