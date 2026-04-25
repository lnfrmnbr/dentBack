package com.example.dent.repository

import com.example.dent.model.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(private val jdbc: JdbcTemplate) {

    private val mapper: RowMapper<User> = RowMapper { rs, _ ->
        User(
            id = rs.getObject("id", UUID::class.java),
            email = rs.getString("email"),
            passwordHash = rs.getString("password_hash"),
            salt = rs.getString("salt"),
            doctorId = rs.getObject("doctor_id", UUID::class.java)
        )
    }

    fun findById(id: UUID): User? =
        jdbc.query("SELECT * FROM users WHERE id = ?", mapper, id).firstOrNull()

    fun findByEmail(email: String): User? =
        jdbc.query("SELECT * FROM users WHERE email = ?", mapper, email).firstOrNull()

    fun save(user: User): User {
        val id = jdbc.queryForObject(
            "INSERT INTO users(email, password_hash, salt, doctor_id) " +
                    "VALUES (?, ?, ?, ?) RETURNING id",
            UUID::class.java,
            user.email,
            user.passwordHash,
            user.salt,
            user.doctorId
        )
        return user.copy(id = id)
    }
}