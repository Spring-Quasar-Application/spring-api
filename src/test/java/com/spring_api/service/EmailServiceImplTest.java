package com.spring_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.spring_api.Main;
import com.spring_api.entity.EmailDetails;

import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@EnableWebSecurity
@AutoConfigureMockMvc
public class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl();

        emailService.setJavaMailSender(javaMailSender);
    }

    @Test
    @WithMockUser(username = "testuser", authorities = { "ROLE_ADMIN" })
    public void testSendMail() throws Exception {
        EmailDetails details = new EmailDetails();
        details.setRecipient("test@example.com");
        details.setSubject("Test Email");
        details.setMsgBody("This is a test email.");

        MimeMessage mockMimeMessage = Mockito.mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mockMimeMessage);
        String result = emailService.sendSimpleMail(details);

        assertEquals("Mail Sent Successfully...", result);
    }

}
