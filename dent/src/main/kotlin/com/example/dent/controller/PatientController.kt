package com.example.dent.controller

import com.example.dent.model.Patient
import com.example.dent.service.PatientService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/patients")
class PatientController(private val service: PatientService) {

    @GetMapping
    fun getAll(): List<Patient> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): Patient? = service.getById(id)

    @PostMapping
    fun create(@RequestBody patient: Patient): Patient = service.create(patient)
}
