package dssd.server.service;

import dssd.server.model.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Async("mailSenderExecutor")
    protected void sendVerificationEmail(Usuario user, String siteURL, String randomCode, String msg)
            throws MessagingException, UnsupportedEncodingException {
        try {
            String toAddress = user.getEmail();
            String fromAddress = "lautaromoller345@gmail.com";
            String senderName = "Ecocycle";
            String subject = "Confirmar cuenta";
            String content = "Estimado [[name]],<br>"
                    + "Haz click en el sigueiente enlace para " + msg + " tu cuenta:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Verificar</a></h3>"
                    + "Muchas gracias,<br>"
                    + "Atentamente el equipo de CuentasClaras.";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getApellido());
            String verifyURL = siteURL + "?code=" + randomCode;

            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);

            mailSender.send(message);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
