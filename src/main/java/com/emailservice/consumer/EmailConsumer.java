package com.emailservice.consumer;

import com.emailservice.model.EmailRequestEvent;
import com.emailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "email-request-topic", groupId = "email-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EmailRequestEvent event) {
        System.out.println("📨 Recebido evento de email [Genérico]: " + event);
        mailService.sendEmail(event);
    }
}
