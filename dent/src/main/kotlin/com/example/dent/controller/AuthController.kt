package com.example.dent.controller

import com.example.dent.model.User
import com.example.dent.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController(private val service: UserService) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): User? = service.getById(id)

    @GetMapping("/by-email")
    fun getByEmail(@RequestParam email: String): User? = service.getByEmail(email)

    @PostMapping("/register")
    fun create(@RequestBody req: RegisterRequest): ResponseEntity<Any> {
        val tokens = service.register(req.email, req.password, req.fullName, req.firstName, req.sex)
        return ResponseEntity.ok(tokens)
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<Any> {
        return try {
            val tokens = service.login(req.email, req.password)
            ResponseEntity.ok(tokens)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to "Invalid credentials"))
        }
    }

    data class RegisterRequest(
        val email: String,
        val password: String,
        val fullName: String,
        val firstName: String?,
        val sex: String?
    )

    data class LoginRequest(val email: String, val password: String)
}
