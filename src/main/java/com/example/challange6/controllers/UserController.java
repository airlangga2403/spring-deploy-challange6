package com.example.challange6.controllers;


import com.example.challange6.dto.user.JwtResponse;
import com.example.challange6.dto.user.request.UserRequestChangePWDTO;
import com.example.challange6.dto.user.request.UserRequestDTO;
import com.example.challange6.dto.user.response.UserResponseByIdDTO;
import com.example.challange6.dto.user.response.UserResponseChangePW;
import com.example.challange6.dto.user.response.UserResponseDTO;
import com.example.challange6.dto.user.response.UserResponseListDTO;
import com.example.challange6.exception.UserRegistrationException;
import com.example.challange6.security.jwt.JwtUtils;
import com.example.challange6.security.service.UserDetailsImpl;
import com.example.challange6.services.InvoiceService;
import com.example.challange6.services.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UsersService userService;
    private final InvoiceService invoiceService;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    @Autowired
    public UserController(UsersService userService, InvoiceService invoiceService, AuthenticationManager authenticationManager, JwtUtils jwtUtils ) {
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userDTO) {
        try {
            UserRequestDTO registeredUser = userService.registerUser(userDTO);

            if (registeredUser != null) {
                log.info("Registration successful for user: {}", registeredUser.getUsername());
                return ResponseEntity.ok(new UserResponseDTO(registeredUser.getUuid(), "Registration successful", null));
            } else {
                log.error("Registration failed for user: {}", userDTO.getUsername());
                return ResponseEntity.ok(new UserResponseDTO(null, "Registration Failed", null));
            }
        } catch (UserRegistrationException e) {
            log.error("Registration failed for user: {}", userDTO.getUsername(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponseDTO(null, e.getMessage(), null));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody UserRequestDTO userDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUsername(), roles);

            if (jwtResponse != null) {
                log.info("Login successful for user: {}", userDetails.getUsername());
                return ResponseEntity.ok(new UserResponseDTO(null, "Login successful", jwtResponse));
            } else {
                log.warn("Login failed for user: {}", userDTO.getUsername());
                return ResponseEntity.ok(new UserResponseDTO(null, "Login failed. Invalid credentials.", null));
            }
        } catch (BadCredentialsException e) {
            log.warn("Login failed due to bad credentials for user: {}", userDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponseDTO(null, "Login failed. Invalid credentials.", null));
        } catch (Exception e) {
            log.error("Internal server error while processing login for user: {}", userDTO.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserResponseDTO(null, "Internal server error", null));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseByIdDTO> getUserByID(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id); // Lakukan validasi terlebih dahulu
            UserResponseByIdDTO userResponseByIdDTO = userService.getUserByUUID(uuid);
            return ResponseEntity.ok(userResponseByIdDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("")
    public ResponseEntity<UserResponseChangePW> updateUserPasswordByEmail(@RequestBody UserRequestChangePWDTO userDTO) {
        try {
            UserResponseChangePW userChangePW = userService.changePwByUsername(userDTO);
            return ResponseEntity.ok(userChangePW);
        } catch (Exception e) {
            log.error("Internal server error while processing login for user: {}", userDTO.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserResponseChangePW("Internal server error " + e.getMessage()));
        }
    }

    @GetMapping("/ordered-food")
    public ResponseEntity<?> getListUserOrderedFood() {
        List<UserResponseListDTO> orderedFoodList = userService.getListUserOrderedFood();
        if (orderedFoodList.isEmpty()) {
            log.error("Products not found.");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else {
            return ResponseEntity.ok(orderedFoodList);
        }
    }

    @PostMapping("/generateInvoice/{userId}")
    public ResponseEntity<?> getInvoiceById(@PathVariable UUID userId) {
        byte[] pdfBytes = invoiceService.generateInvoice(userId);

        if (pdfBytes != null) {
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
