package com.flordacidade.api.flor_da_cidade_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties; 

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    public EmailService(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @PostConstruct
    public void init() {
        logger.info("EmailService inicializado. Remetente configurado: '{}'", mailProperties.getUsername());
    }

    @Async
    public void sendPasswordResetEmail(String to, String token) {

        String finalFrontendUrl = frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
        String resetUrl = finalFrontendUrl + "/redefinir-senha/" + token;

        logger.info("Construindo e-mail de redefinição. URL de reset: '{}'", resetUrl);

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(mailProperties.getUsername());
            message.setTo(to);
            message.setSubject("Redefinição de Senha - Flor da Cidade");
            message.setText(
                    "Olá,\n\n"
                            + "Você solicitou a redefinição da sua senha. Por favor, clique no link abaixo para criar uma nova senha:\n\n"
                            + resetUrl + "\n\n" 
                            + "Se você não solicitou esta alteração, por favor, ignore este e-mail.\n\n"
                            + "Atenciosamente,\nEquipe Flor da Cidade"
            );

            mailSender.send(message);
            logger.info("E-mail de redefinição de senha enviado com sucesso para: {}", to);

        } catch (MailException e) {
            logger.error("Falha ao enviar e-mail de redefinição para: {}. Causa: {}", to, e.getMessage(), e);
        }
    }
}
