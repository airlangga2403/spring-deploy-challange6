package com.example.challange6.repository;


import com.example.challange6.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {

    @Query(value = "SELECT " +
            "o.completed, " +
            "o.order_time, " +
            "o.id, " +
            "o.user_id, " +
            "o.destination_address, " +
            "FLOOR((EXTRACT(DAY FROM o.order_time) - 1) / 7) + 1 AS week_number, " +
            "EXTRACT(MONTH FROM o.order_time) AS month_number, " +
            "SUM(od.total_price) OVER (PARTITION BY FLOOR((EXTRACT(DAY FROM o.order_time) - 1) / 7) + 1, EXTRACT(MONTH FROM o.order_time)) AS income " +
            "FROM orders o " +
            "JOIN order_detail od ON o.id = od.order_id " +
            "ORDER BY o.order_time", nativeQuery = true)
    List<Object[]> findAllWithWeekAndMonthNumbers();

    List<Orders> findByUserId(UUID userId);

}