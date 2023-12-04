package com.example.challange6.dto.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String emailAddress;
    private String password;
}
