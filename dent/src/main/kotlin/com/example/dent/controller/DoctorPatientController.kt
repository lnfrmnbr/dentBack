package com.example.dent.controller

import com.example.dent.service.DoctorPatientService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/doctor-patients")
class DoctorPatientController(private val service: DoctorPatientService) {

    @PostMapping
    fun assign(@RequestParam doctorId: UUID, @RequestParam patientId: UUID) =
        service.assign(doctorId, patientId)

    @GetMapping("/by-doctor/{doctorId}")
    fun getByDoctor(@PathVariable doctorId: UUID) = service.getByDoctorId(doctorId)
}
