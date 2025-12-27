package com.example.dent.service

import com.example.dent.model.AuthToken
import com.example.dent.repository.AuthTokenRepository
import org.springframework.stereotype.Service

@Service
class AuthTokenService(private val repo: AuthTokenRepository) {
    fun getByToken(token: String): AuthToken? = repo.findByToken(token)
    fun create(token: AuthToken): AuthToken = repo.save(token)
}
