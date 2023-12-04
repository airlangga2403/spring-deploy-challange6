package com.example.challange6.dto.user.response;


import com.example.challange6.dto.user.JwtResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID uuid;
    private String message;
    private JwtResponse jwtResponse;
}
