package com.example.dent.controller

import com.example.dent.model.Doctor
import com.example.dent.service.DoctorService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/doctors")
class DoctorController(private val service: DoctorService) {

    @GetMapping
    fun getAll(): List<Doctor> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): Doctor? = service.getById(id)

    @GetMapping("/by_user/{userId}")
    fun getByUserId(@PathVariable userId: UUID): Doctor? = service.getByUserId(userId)

    @PostMapping
    fun create(@RequestBody doctor: Doctor): Doctor = service.create(doctor)
}