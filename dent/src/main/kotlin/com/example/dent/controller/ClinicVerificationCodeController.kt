package com.example.dent.controller

import com.example.dent.service.ClinicVerificationCodeService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/verify")
class ClinicVerificationCodeController(private val service: ClinicVerificationCodeService) {

    @PostMapping("/get")
    fun getVerificationCode(@RequestBody req: GetVerificationCodeRequest) =
        service.getVerificationCode(req.clinicId, req.fio, req.newUserEmail, req.clinicEmail)

    @GetMapping("/check")
    fun checkVerificationCode(@RequestBody req: VerifyCodeRequest): Boolean =
        service.checkVerificationCode(req.clinicId, req.newUserEmail, req.code)

    data class GetVerificationCodeRequest(val clinicId: UUID, val fio: String, val newUserEmail: String, val clinicEmail: String)
    data class VerifyCodeRequest(val clinicId: UUID, val newUserEmail: String, val code: String)
}