package com.example.challange6.repository;

import com.example.challange6.models.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchants, UUID> {

    @Query("SELECT m FROM Merchants m WHERE m.open = true ")
    List<Merchants> getOpenMerchants();

}
