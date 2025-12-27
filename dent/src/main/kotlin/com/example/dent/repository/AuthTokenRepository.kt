package com.example.dent.repository

import com.example.dent.model.AuthToken
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
class AuthTokenRepository(private val jdbc: JdbcTemplate) {

    private val mapper: RowMapper<AuthToken> = RowMapper { rs, _ ->
        AuthToken(
            id = rs.getObject("id", UUID::class.java),
            userId = rs.getObject("user_id", UUID::class.java),
            token = rs.getString("token"),
            tokenType = rs.getString("token_type"),
            expiresAt = rs.getTimestamp("expires_at").toLocalDateTime(),
            createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
            revokedAt = rs.getTimestamp("revoked_at")?.toLocalDateTime()
        )
    }

    fun findByToken(token: String): AuthToken? =
        jdbc.query("SELECT * FROM auth_tokens WHERE token = ?", mapper, token).firstOrNull()

    fun save(authToken: AuthToken): AuthToken {
        val id = jdbc.queryForObject(
            "INSERT INTO auth_tokens(user_id, token, token_type, expires_at, created_at, revoked_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id",
            UUID::class.java,
            authToken.userId,
            authToken.token,
            authToken.tokenType,
            Timestamp.valueOf(authToken.expiresAt),
            Timestamp.valueOf(authToken.createdAt),
            authToken.revokedAt?.let { Timestamp.valueOf(it) }
        )
        return authToken.copy(id = id)
    }
}
