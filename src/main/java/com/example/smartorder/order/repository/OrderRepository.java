package com.example.smartorder.order.repository;

import com.example.smartorder.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) { this.em.persist(order); }

    public Order findById(String id) { return this.em.find(Order.class, id); }
}
