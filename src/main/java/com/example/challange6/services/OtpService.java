package com.example.challange6.services;

import com.example.challange6.dto.user.request.OtpValidationRequest;

import java.util.concurrent.CompletableFuture;

public interface OtpService {
    String generateOTP(String accountNumber);
    CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp);
    boolean validateOTP(OtpValidationRequest otpValidationRequest);
}
