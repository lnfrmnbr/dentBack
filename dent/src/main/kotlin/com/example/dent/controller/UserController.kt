package com.example.dent.controller

import com.example.dent.model.User
import com.example.dent.service.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController(private val service: UserService) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): User? = service.getById(id)

    @GetMapping("/by-email")
    fun getByEmail(@RequestParam email: String): User? = service.getByEmail(email)

    @PostMapping
    fun create(@RequestBody user: User): User = service.create(user)
}
