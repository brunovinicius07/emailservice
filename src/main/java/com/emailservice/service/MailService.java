package com.emailservice.service;

import com.emailservice.model.EmailRequestEvent;
import com.emailservice.model.EmailType;
import static com.emailservice.model.EmailType.FORGOT_PASSWORD;
import static com.emailservice.model.EmailType.VERIFICATION_CODE;
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

    public void sendEmail(EmailRequestEvent event) {
        try {
            switch (event.getType()) {
                case FORGOT_PASSWORD -> sendForgotPasswordEmail(event);
                case VERIFICATION_CODE -> sendVerificationCodeEmail(event);
                default -> System.out.println("⚠️ Tipo de e-mail não suportado: " + event.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao processar e-mail: " + e.getMessage());
        }
    }

    private void sendForgotPasswordEmail(EmailRequestEvent event) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(event.getTo());
        helper.setSubject("Redefinição de Senha - PlayIt");

        String token = (String) event.getProps().get("token");
        String nameUser = (String) event.getProps().getOrDefault("name", "Usuário");
        String expiration = (String) event.getProps().getOrDefault("expiration", "5 minutos");

        String resetLink = "http://localhost:4200/reset-password?token=" + token;

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
                                Caso o botão não funcione, clique no link alternativo abaixo:<br/><br/>
                                <a href="%s" style="color:#1E2772; font-weight: bold; text-decoration: underline;">Link de Redefinição</a>
                            </td>
                        </tr>

                        <tr>
                            <td style="padding-top: 35px; color:#999; text-align:center; font-size: 13px;">
                                Este link expira em <strong>%s</strong>.
                            </td>
                        </tr>

                    </table>
                </body>
                </html>
                """
                .formatted(nameUser, resetLink, resetLink, expiration);

        helper.setText(html, true);
        addInlineResources(helper);

        mailSender.send(mimeMessage);
        System.out.println("Email FORGOT_PASSWORD enviado para: " + event.getTo());
    }

    private void sendVerificationCodeEmail(EmailRequestEvent event) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(event.getTo());
        helper.setSubject(event.getSubject());

        String code = (String) event.getProps().get("code");
        String expiration = (String) event.getProps().get("expiration");

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
                                    Código de Verificação
                                </h2>

                                <p style="color:#555555; font-size: 16px;">
                                    Utilize o código abaixo para validar sua conta:
                                </p>
                            </td>
                        </tr>

                        <tr>
                            <td style="text-align: center; padding: 30px 0;">
                                <div style="
                                    background-color: #f0f0f5;
                                    color: #7001FD;
                                    padding: 14px 34px;
                                    border-radius: 10px;
                                    font-size: 24px;
                                    font-weight: bold;
                                    letter-spacing: 4px;
                                    display: inline-block;
                                    border: 2px dashed #7001FD;
                                ">
                                    %s
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td style="padding-top: 20px; color:#999; text-align:center; font-size: 13px;">
                                Este código expira em <strong>%s</strong>.
                            </td>
                        </tr>

                    </table>
                </body>
                </html>
                """
                .formatted(code, expiration);

        helper.setText(html, true);
        addInlineResources(helper);

        mailSender.send(mimeMessage);
        System.out.println("Email VERIFICATION_CODE enviado para: " + event.getTo());
    }

    private void addInlineResources(MimeMessageHelper helper) throws Exception {
        helper.addInline("logo", new ClassPathResource("static/images/logo.png"));
        helper.addInline("guitar", new ClassPathResource("static/images/guitar.png"));
    }

}
