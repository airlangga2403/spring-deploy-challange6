package com.example.challange6.controllers;


import com.example.challange6.dto.order.request.CreateOrderRequestDTO;
import com.example.challange6.dto.order.response.CreateOrderResponseDTO;
import com.example.challange6.dto.order.response.GetOrderResponseDTO;
import com.example.challange6.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

;

@RestController
@Slf4j
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable UUID userId, @RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        log.info("Received a request to create an order for user with ID: {}", userId);
        CreateOrderResponseDTO responseDTO = orderService.createOrder(userId, createOrderRequestDTO);

        if (responseDTO != null) {
            log.info("Response from createOrder: {}", responseDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            log.error("User not found or unable to create an order.");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "User not found or unable to create an order");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetOrderResponseDTO>> getAllOrders() {
        log.info("Received a request to get all orders.");
        List<GetOrderResponseDTO> orderResponses = orderService.getAllOrders();
        log.info("Response from getAllOrders: {}", orderResponses);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }
}