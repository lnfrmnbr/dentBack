package com.example.dent.service

import com.example.dent.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secret: String
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateAccessToken(user: User): String =
        Jwts.builder()
            .setSubject(user.id.toString())
            .claim("email", user.email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 15 * 60 * 1000))
            .signWith(key)
            .compact()
}