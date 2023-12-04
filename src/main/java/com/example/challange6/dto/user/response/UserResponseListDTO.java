package com.example.challange6.dto.user.response;


import com.example.challange6.models.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseListDTO {
    private UUID id;
    private String username;
    private String emailAddress;

    private List<Orders> orders;
}
