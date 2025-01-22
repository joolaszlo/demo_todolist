package joo.demo.todolist.service.impl;

import joo.demo.todolist.domain.Token;
import joo.demo.todolist.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static joo.demo.todolist.config.ApplicationConfiguration.*;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private static String SENDER;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendRegistrationEmail(Token token) {
        String body = EMAIL_REGISTRATION_BODY + SITE_URL + "confirmregistration?token=" + token.getToken();
        sendEmail(token.getEmail(), "Registration confirmation", body);
    }

    @Override
    public void sendPasswordResetEmail(Token token) {
        String body = EMAIL_PASSWORD_RESET_BODY + SITE_URL + "changepassword?token=" + token.getToken();

        sendEmail(token.getEmail(), "Password reset", body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
