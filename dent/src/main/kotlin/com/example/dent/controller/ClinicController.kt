package com.example.dent.controller

import com.example.dent.model.Clinic
import com.example.dent.service.ClinicService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/clinics")
class ClinicController(private val service: ClinicService) {

    @GetMapping
    fun getAll(): List<Clinic> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): Clinic? = service.getById(id)

    @PostMapping
    fun create(@RequestBody patient: Clinic): Clinic = service.create(patient)
}