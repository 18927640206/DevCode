package org.luisitobez.burgerved.model.data

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*

class EmailSender(
    private val config: EmailConfig = EmailConfig()
) {
    fun enviarAlertaStock(ingredientesAgotados: List<String>): Boolean {
        return try {
            val props = Properties().apply {
                put("mail.smtp.host", config.smtpHost)
                put("mail.smtp.port", config.smtpPort)
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
            }

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(config.smtpUser, config.smtpPassword)
                }
            })

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(config.smtpUser))
                addRecipient(Message.RecipientType.TO, InternetAddress(config.adminEmail))
                subject = "🚨 Alerta: Ingredientes agotados en BurgerVend"

                val cuerpo = StringBuilder().apply {
                    appendLine("Los siguientes ingredientes están agotados:")
                    ingredientesAgotados.forEach { appendLine("    - $it") }
                    appendLine()
                    appendLine("Fecha: ${Date()}")
                }.toString()

                setText(cuerpo)
            }

            Transport.send(message)
            true
        } catch (e: Exception) {
            System.err.println("Error enviando correo: ${e.message}")
            false
        }
    }
}

data class EmailConfig(
    val smtpHost: String = "sandbox.smtp.mailtrap.io", //se usa mailtrapio para las pruebas
    val smtpPort: String = "587",
    val smtpUser: String = "03aa1bf00faee1", //
    val smtpPassword: String = "810162d5d5e57a",  //
    val adminEmail: String = "admin@burgerved.com" //email no existente
)