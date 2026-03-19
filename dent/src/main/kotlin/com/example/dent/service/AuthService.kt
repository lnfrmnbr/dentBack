package com.example.dent.service

import com.example.dent.model.AuthToken
import com.example.dent.model.Doctor
import com.example.dent.model.User
import com.example.dent.repository.AuthTokenRepository
import com.example.dent.repository.DoctorRepository
import com.example.dent.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*
import org.springframework.security.crypto.bcrypt.BCrypt
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val doctorRepository: DoctorRepository,
    private val tokenRepository: AuthTokenRepository,
    private val jwt: JwtService
) {
    fun getById(id: UUID): User? = userRepository.findById(id)
    fun getByEmail(email: String): User? = userRepository.findByEmail(email)
    fun register(
        email: String,
        rawPassword: String,
        lastName: String,
        firstName: String,
        patronymic: String?,
        sex: String?,
        role: String,
        birthDate: Date
    ): Tokens {
        val salt = BCrypt.gensalt()
        val passwordHash = BCrypt.hashpw(rawPassword, salt)

        val doctorId = doctorRepository.save(
            Doctor(
                firstName = firstName,
                lastName = lastName,
                patronymic = patronymic,
                sex = sex,
                role = role,
                birthDate = birthDate
            )
        ).id

        val user = userRepository.save(User(
            email = email,
            doctorId = doctorId!!,
            passwordHash = passwordHash,
            salt = salt,
            providerId = null
        ))

        return generateTokens(user)
    }

    private fun generateTokens(user: User): Tokens {
        val access = jwt.generateAccessToken(user)
        val refresh = UUID.randomUUID().toString()

        tokenRepository.save(
            AuthToken(
                userId = user.id!!,
                token = refresh,
                expiresAt = LocalDateTime.now().plusDays(30)
            )
        )

        return Tokens(access, refresh)
    }

    fun login(email: String, password: String): Tokens {
        val auth = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Invalid credentials")

        if (!BCrypt.checkpw(password, auth.passwordHash))
            throw IllegalArgumentException("Invalid credentials")

        val user = userRepository.findById(auth.id!!)!!
        return generateTokens(user)
    }
}

data class Tokens(
    val access: String,
    val refresh: String
)
