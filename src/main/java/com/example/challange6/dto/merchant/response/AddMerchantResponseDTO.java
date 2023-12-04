package com.example.challange6.dto.merchant.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AddMerchantResponseDTO {
    private UUID id;
    private String merchantName;
    private String merchantLocation;
    private String message;

}
