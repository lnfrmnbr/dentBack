package com.example.dent.controller

import com.example.dent.model.AuthToken
import com.example.dent.service.AuthTokenService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth-tokens")
class AuthTokenController(private val service: AuthTokenService) {

    @GetMapping("/{token}")
    fun getByToken(@PathVariable token: String): AuthToken? = service.getByToken(token)

    @PostMapping
    fun create(@RequestBody token: AuthToken): AuthToken = service.create(token)
}
