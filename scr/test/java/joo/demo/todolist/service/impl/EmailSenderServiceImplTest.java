package joo.demo.todolist.service.impl;

import joo.demo.todolist.config.ApplicationConfiguration;
import joo.demo.todolist.domain.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailSenderServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new EmailSenderServiceImpl(mailSender);

        ApplicationConfiguration.EMAIL_REGISTRATION_BODY = "testbody";
        ApplicationConfiguration.EMAIL_PASSWORD_RESET_BODY = "testbody";
        ApplicationConfiguration.SITE_URL = "testurl";
    }

    @Test
    void sendRegistrationEmail() {
        Token token = new Token();
        token.setEmail("test@email.com");
        token.setToken("testtoken");

        underTest.sendRegistrationEmail(token);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendPasswordResetEmail() {
        Token token = new Token();
        token.setEmail("test@email.com");
        token.setToken("testtoken");

        underTest.sendPasswordResetEmail(token);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}