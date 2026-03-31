package com.example.dent.controller

import com.example.dent.model.Appointment
import com.example.dent.service.AppointmentService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/appointments")
class AppointmentController(private val service: AppointmentService) {

    @GetMapping("/for_doc/{doctorId}")
    fun findAllForDoctor(@PathVariable doctorId: UUID): List<Appointment> = service.findAllForDoctor(doctorId)

    @GetMapping("/upcoming/{doctorId}")
    fun findUpcomingForDoctor(@PathVariable doctorId: UUID): List<Appointment> = service.findUpcomingForDoctor(doctorId)

    @GetMapping("/archive/{doctorId}")
    fun findArchiveForDoctor(@PathVariable doctorId: UUID): List<Appointment> = service.findArchiveForDoctor(doctorId)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): Appointment? = service.getById(id)

    @PostMapping
    fun create(@RequestBody appointment: Appointment): Appointment = service.create(appointment)
}
