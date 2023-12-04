package com.example.challange6.repository;

import com.example.challange6.models.ERole;
import com.example.challange6.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole roleName);
}
