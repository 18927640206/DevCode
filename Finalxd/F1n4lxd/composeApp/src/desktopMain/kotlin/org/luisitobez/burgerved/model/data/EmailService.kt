package org.luisitobez.burgerved.model.data

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.luisitobez.burgerved.model.domain.Reporte
import java.util.*

data class EmailConfig(
    val smtpHost: String = "smtp.gmail.com",
    val smtpPort: String = "587",
    val smtpUser: String,
    val smtpPassword: String,
    val fromEmail: String = smtpUser
)

class EmailService(private val config: EmailConfig) {
    private val props = Properties().apply {
        put("mail.smtp.host", config.smtpHost)
        put("mail.smtp.port", config.smtpPort)
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.ssl.trust", config.smtpHost)
    }

    private val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(config.smtpUser, config.smtpPassword)
        }
    })

    fun enviarReporte(destinatario: String, reporte: Reporte): Boolean {
        return try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(config.fromEmail))
                addRecipient(Message.RecipientType.TO, InternetAddress(destinatario))
                subject = "Nuevo Reporte de Problema - Pedido #${reporte.idPedido}"

                val texto = """
                    ===== REPORTE DE PROBLEMA =====
                    
                    Pedido ID: ${reporte.idPedido}
                    Tipo de problema: ${reporte.problema.descripcion}
                    Contacto del cliente: ${reporte.contactoCliente}
                    
                    Descripción:
                    ${reporte.descripcion}
                    
                    Fecha del reporte: ${reporte.fecha}
                    
                    ===== BurgerVed =====
                """.trimIndent()

                setText(texto)
            }

            Transport.send(message)
            true
        } catch (e: Exception) {
            System.err.println("Error enviando correo: ${e.message}")
            false
        }
    }

    companion object {
        fun createDefault(): EmailService {
            return EmailService(EmailConfig(
                smtpHost = "sandbox.smtp.mailtrap.io",
                smtpPort = "587",
                smtpUser = "03aa1bf00faee1",
                smtpPassword = "810162d5d5e57a",
                fromEmail = "no-reply@burgerved.com"
            ))
        }
    }
}
