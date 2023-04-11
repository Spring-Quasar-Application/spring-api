package com.spring_api.service;

import com.spring_api.entity.EmailDetails;

public interface EmailService {
 
    String sendSimpleMail(EmailDetails details);
 
    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}