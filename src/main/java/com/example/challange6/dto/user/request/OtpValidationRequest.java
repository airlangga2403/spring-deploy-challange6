package com.example.challange6.dto.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpValidationRequest {
    private String uuid;
    private String otp;
}
