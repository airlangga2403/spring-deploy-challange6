package com.example.challange6.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponseDTO {
    private UUID id;
    private Date order_time;
    private String destinationAddress;
    private boolean completed;

}