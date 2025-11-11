package com.emailservice.consumer;

import com.emailservice.model.ForgotPasswordEvent;
import com.emailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForgotPasswordConsumer {

    private final MailService mailService;

    @KafkaListener(
            topics = "forgot-password-topic",
            groupId = "email-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ForgotPasswordEvent event) {
        System.out.println("📨 Recebido evento de esqueci a senha: " + event);
        mailService.sendForgotPasswordEmail(event);
    }
}
