package com.example.challange6.repository;

import com.example.challange6.models.OtpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OtpInfoRepository extends JpaRepository<OtpInfo, UUID> {
    OtpInfo findByEmailUser(String emailUser);
}
