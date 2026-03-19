package com.example.dent.controller

import com.example.dent.model.Appointment
import com.example.dent.service.AppointmentService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/appointments")
class AppointmentController(private val service: AppointmentService) {

    @GetMapping("/forDoc/{doctorId}")
    fun findAllForDoctor(@PathVariable doctorId: UUID): List<Appointment> = service.findAllForDoctor(doctorId)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): Appointment? = service.getById(id)

    @PostMapping
    fun create(@RequestBody appointment: Appointment): Appointment = service.create(appointment)
}
