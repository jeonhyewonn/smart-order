package com.example.smartorder.order.repository;

import com.example.smartorder.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN o.member m WHERE o.id = :id")
    public Optional<Order> findById(@Param("id") Long id);
}