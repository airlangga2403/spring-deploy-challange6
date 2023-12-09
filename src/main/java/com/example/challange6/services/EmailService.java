package com.example.challange6.services;

import com.example.challange6.models.EmailDetails;

public interface EmailService {
    String sendEmail(EmailDetails emailDetails);

    // send with attachment
    String sendEmailWithAttachment(EmailDetails emailDetails);
}
