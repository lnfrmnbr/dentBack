package com.example.dent.service

import com.example.dent.model.User
import com.example.dent.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val repo: UserRepository) {
    fun getById(id: UUID): User? = repo.findById(id)
    fun getByEmail(email: String): User? = repo.findByEmail(email)
    fun create(user: User): User = repo.save(user)
}
