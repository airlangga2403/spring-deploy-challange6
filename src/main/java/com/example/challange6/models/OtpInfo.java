package com.example.challange6.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "otp_infodb")
public class OtpInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid")
    private UUID uuid;


    @Column(unique = true)
    private String emailUser;

    @Column
    private String otp;

    @Column
    private LocalDateTime generatedAt;
}
