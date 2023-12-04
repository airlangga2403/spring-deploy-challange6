package com.example.challange6.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String username;
    private String emailAddress;
    private String password;
    private int roleUser;
    private UUID uuid;
}