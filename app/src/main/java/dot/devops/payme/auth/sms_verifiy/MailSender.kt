package dot.devops.payme.auth.sms_verifiy

import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object MailSender {
    fun sendEmail(recipientEmail: String, code: String) {
        val host = "smtp.gmail.com"
        val username = "jaloliddinumirzoqov2483@gmail.com"  // Gmail emailingiz
        val password = "01553235abc"  // Gmail app password yoki parolingiz

        // SMTP konfiguratsiyasi
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = "587"

        // Session yaratish
        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            // Xat yozish
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress("jaloliddinumirzoqov2483@gmail.com"))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
            message.subject = "Tasdiqlash Kodi"
            message.setText("Sizning Payme ilovasiga kirish uchun tasdiqlash kodingiz: $code")

            // Elektron pochta yuborish
            Transport.send(message)
            println("Tasdiqlash kodi yuborildi")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
