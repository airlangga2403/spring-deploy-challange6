package com.example.challange6.services;

import com.example.challange6.models.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImpl implements EmailService {


    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            // No need to set the "from" address; it is automatically set by Spring Boot based on your properties
            helper.setSubject(subject);
            helper.setText(text, true); // Set the second parameter to true to send HTML content
            mailSender.send(message);

            future.complete(null); // Indicate that the email sending is successful
        } catch (MessagingException e) {
            e.printStackTrace();
            future.completeExceptionally(e); // Indicate that the email sending failed
        }

        return future;
    }

    @Override
    public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp) {
        // Create the formatted email template with the provided values
        String emailTemplate = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">"
                + "<div style=\"margin:50px auto;width:70%;padding:20px 0\">"
                + "<div style=\"border-bottom:1px solid #eee\">"
                + "<a href=\"https://piggybank.netlify.app/\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">piggybank</a>"
                + "</div>"
                + "<p style=\"font-size:1.1em\">Hi, " + name + "</p>"
                + "<p style=\"font-size:0.9em;\">Account Number: " + accountNumber + "</p>"
                + "<p>Thank you for choosing OneStopBank. Use the following OTP to complete your Log In procedures. OTP is valid for 5 minutes</p>"
                + "<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + otp + "</h2>"
                + "<p style=\"font-size:0.9em;\">Regards,<br />OneStopBank</p>"
                + "<hr style=\"border:none;border-top:1px solid #eee\" />"
                + "<p>piggybank Inc</p>"
                + "<p>1600 Amphitheatre Parkway</p>"
                + "<p>California</p>"
                + "</div>"
                + "</div>";

        return emailTemplate;
    }
}