package com.example.dent.service

import com.example.dent.model.ClinicVerificationCode
import com.example.dent.repository.ClinicVerificationCodeRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service

class ClinicVerificationCodeService(
    private val repo: ClinicVerificationCodeRepository,
    private val emailService: EmailService
) {
    fun getVerificationCode(clinicId: UUID, fio: String, newUserEmail: String, clinicEmail: String) {
        val code = (100000..999999).random().toString()

        emailService.sendClinicVerificationCode(fio, newUserEmail, clinicEmail, code)

        repo.addVerificationCode(
            verificationCode = ClinicVerificationCode(
                clinicId = clinicId,
                code = code,
                email = newUserEmail,
                expiresAt = LocalDateTime.now().plusHours(1),
                verified = false
            )
        )
    }

    fun checkVerificationCode(clinicId: UUID, newUserEmail: String, code: String): Boolean {
        return repo.checkVerificationCode(clinicId, newUserEmail, code)
    }
}