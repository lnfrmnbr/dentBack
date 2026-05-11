package com.example.dent.repository

import com.example.dent.model.ClinicVerificationCode
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ClinicVerificationCodeRepository(private val jdbc: JdbcTemplate) {
    private val mapper: RowMapper<ClinicVerificationCode> = RowMapper { rs, _ ->
        ClinicVerificationCode(
            id = rs.getObject("id", UUID::class.java),
            clinicId = rs.getObject("clinic_id", UUID::class.java),
            code = rs.getString("code"),
            email = rs.getString("email"),
            expiresAt = rs.getTimestamp("expires_at").toLocalDateTime(),
            verified = rs.getBoolean("verified")
        )
    }

    fun checkVerificationCode(
        clinicId: UUID,
        newUserEmail: String,
        code: String
    ): Boolean {

        val verificationCode = jdbc.query(
            """
            SELECT *
            FROM clinic_verification_code
            WHERE clinic_id = ?
              AND email = ?
              AND code = ?
              AND verified = false
              AND expires_at > now()
            LIMIT 1
            """.trimIndent(),
            mapper,
            clinicId,
            newUserEmail,
            code
        ).firstOrNull()

        if (verificationCode != null) {

            jdbc.update(
                """
                UPDATE clinic_verification_code
                SET verified = true
                WHERE id = ?
                """.trimIndent(),
                verificationCode.id
            )

            return true
        }

        return false
    }

    fun addVerificationCode(verificationCode: ClinicVerificationCode): ClinicVerificationCode {
        val id = jdbc.queryForObject(
            "INSERT INTO clinic_verification_code(clinic_id, code, email, expires_at, verified) VALUES (?, ?, ?, ?, ?) RETURNING id",
            UUID::class.java,
            verificationCode.clinicId,
            verificationCode.code,
            verificationCode.email,
            verificationCode.expiresAt,
            verificationCode.verified
        )
        return verificationCode.copy(id = id)
    }
}