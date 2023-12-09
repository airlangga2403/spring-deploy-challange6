package com.example.challange6.repository;

import com.example.challange6.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    @Query("SELECT u FROM Users u WHERE u.username = :username AND u.password = :password")
    Optional<Users> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

//    // UpdateNewUsername
//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query(value = "UPDATE Users SET password = :newPassword WHERE username = :username AND password = :password", nativeQuery = true)
//    Integer updatePasswordByUsername(@Param("username") String username, @Param("password") String password, @Param("newPassword") String newPassword);


    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmailAddress(String email);
}
