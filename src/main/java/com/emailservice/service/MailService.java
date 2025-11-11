package com.emailservice.service;

import com.emailservice.model.ForgotPasswordEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendForgotPasswordEmail(ForgotPasswordEvent event) {
        String resetLink = "http://localhost:4200/reset-password?token=" + event.getToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("Redefinição de senha - PlayIt");
        message.setText(String.format("""
                Olá, %s!

                Recebemos uma solicitação para redefinir sua senha.

                Clique no link abaixo para criar uma nova senha:
                %s

                Este link expira em 2 minutos.
                """, event.getNameUser(), resetLink));

        try {
            mailSender.send(message);
            System.out.println("Email enviado com sucesso para: " + event.getEmail());
        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
        }
    }
}
