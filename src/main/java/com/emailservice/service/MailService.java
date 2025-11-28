package com.emailservice.service;

import com.emailservice.model.ForgotPasswordEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendForgotPasswordEmail(ForgotPasswordEvent event) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject("Redefinição de Senha - PlayIt");

            String resetLink = "http://localhost:4200/reset-password?token=" + event.getToken();

            String html = """
        <html>
        <body style="font-family: Arial, sans-serif; background-color:#F1F3F6; padding: 40px;">
            <table width="100%%" cellspacing="0" cellpadding="0" 
                   style="max-width: 600px; margin: auto; background: #FFFFFF; border-radius: 16px; padding: 30px;
                   box-shadow: 0 4px 20px rgba(0,0,0,0.06);">

                <tr>
                    <td style="text-align:center;">
                        <img src="cid:logo" width="160" alt="PlayIt Logo" />
                    </td>
                </tr>

                <tr>
                    <td style="text-align:center; padding-top: 10px;">
                        <img src="cid:guitar" width="240" alt="Guitar Illustration" />
                    </td>
                </tr>

                <tr>
                    <td style="padding-top: 20px; text-align: center;">
                        <h2 style="color:#7001FD; margin-bottom: 10px; font-size: 26px;">
                            Olá, %s!
                        </h2>

                        <p style="color:#555555; font-size: 16px;">
                            Recebemos uma solicitação para redefinir sua senha.
                        </p>

                        <p style="color:#555555; font-size: 16px;">
                            Clique no botão abaixo para criar uma nova senha:
                        </p>
                    </td>
                </tr>

                <tr>
                    <td style="text-align: center; padding: 30px 0;">
                        <a href="%s" style="
                            background-color: #7001FD;
                            color: white;
                            padding: 14px 34px;
                            border-radius: 10px;
                            text-decoration: none;
                            font-size: 16px;
                            font-weight: bold;
                            display: inline-block;
                            box-shadow: 0 4px 12px rgba(112,1,253,0.3);
                        ">
                            Redefinir Senha
                        </a>
                    </td>
                </tr>

                <tr>
                    <td style="color:#777; text-align:center; font-size: 14px; padding: 0 20px;">
                        Caso o botão não funcione, copie e cole o link abaixo no seu navegador:<br/><br/>
                        <span style="word-break: break-all; color:#1E2772;">%s</span>
                    </td>
                </tr>

                <tr>
                    <td style="padding-top: 35px; color:#999; text-align:center; font-size: 13px;">
                        Este link expira em <strong>5 minutos</strong>.
                    </td>
                </tr>

            </table>
        </body>
        </html>
        """.formatted(event.getNameUser(), resetLink, resetLink);

            helper.setText(html, true);

            helper.addInline("logo", new ClassPathResource("static/images/logo.png"));
            helper.addInline("guitar", new ClassPathResource("static/images/guitar.png"));

            mailSender.send(mimeMessage);

            System.out.println("Email HTML enviado para: " + event.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao enviar HTML email: " + e.getMessage());
        }
    }

}
