package com.example.dent.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    @Value("\${spring.mail.username}") private val fromEmail: String
) {

    fun sendClinicVerificationCode(
        fio: String, newUserEmail: String, clinicEmail: String, code: String
    ) {
        val message = SimpleMailMessage()
        message.from = fromEmail
        message.setTo(clinicEmail)
        message.subject = "Код подтверждения сотрудника"
        message.text = """
            Здравствуйте!
            
            Необходимо подтвердить принадлежность сотрудника $fio ($newUserEmail) к Вашей организации. Если Вы не знаете этого человека, проигнорируйте это письмо. Будьте осторожны — получив код подтверждения, $fio получит доступ к данным пациентов Вашей клиники в приложении OriScope.
            
            Код подтверждения: $code
        """.trimIndent()

        mailSender.send(message)
    }
}