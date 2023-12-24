package com.example.challange6.services;

import com.example.challange6.dto.user.request.OtpValidationRequest;
import com.example.challange6.models.EmailDetails;
import com.example.challange6.models.OtpInfo;
import com.example.challange6.repository.OtpInfoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@Service
@Log4j2
public class OtpServiceImpl implements OtpService{

    @Autowired
    private OtpInfoRepository otpInfoRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public String generateOTP(String emailUser) {
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);

        OtpInfo otpInfo = new OtpInfo();
        otpInfo.setEmailUser(emailUser);
        otpInfo.setOtp(otp);
        otpInfo.setGeneratedAt(LocalDateTime.now());

        otpInfoRepository.save(otpInfo);
        return otp;
    }
    @Override
    @Async
    public CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp) {
        // Compose the email content
        String subject = "OTP Verification";
        String emailText = emailService.getOtpLoginEmailTemplate(name, "xxx" + accountNumber.substring(3), otp);

        CompletableFuture<Void> emailSendingFuture = emailService.sendEmail(email, subject, emailText);

        return emailSendingFuture.thenApplyAsync(result -> true)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }
    @Override
    public boolean validateOTP(OtpValidationRequest otp) {
        OtpInfo otpInfo = otpInfoRepository.findByEmailUser(otp.getUuid());
        log.info("uuid email"+ otp.getUuid() + otp.getOtp());

        if (otpInfo.getEmailUser().equals(otp.getUuid()) && otpInfo.getOtp().equals(otp.getOtp())) {
            return true;
        } else {
            return false;
        }
    }


}
