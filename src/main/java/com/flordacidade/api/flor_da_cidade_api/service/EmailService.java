package com.flordacidade.api.flor_da_cidade_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Async
    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = frontendUrl + "/redefinir-senha?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject("Redefinição de Senha - Flor da Cidade");
        message.setText("Para redefinir sua senha, clique no link abaixo:\n\n"
                + resetUrl + "\n\n"
                + "Se você não solicitou uma redefinição de senha, por favor ignore este e-mail.");

        mailSender.send(message);
    }
}
