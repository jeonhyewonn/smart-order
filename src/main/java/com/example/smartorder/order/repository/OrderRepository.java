package com.example.smartorder.order.repository;

import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) { this.em.persist(order); }

    public Order findById(String id) {
        Optional<Order> result = this.findOrderByOrderId(id);
        if (result.isEmpty()) return null;

        Order order = result.get();

        order.getOrderItems().clear();
        for (OrderItem orderItem : this.findOrderItemsByOrderId(order.getId())) {
            order.getOrderItems().add(orderItem);
        }

        return order;
    }

    private Optional<Order> findOrderByOrderId(String orderId) {
        return this.em.createQuery(
                        "SELECT distinct o " +
                                "FROM Order o " +
                                "JOIN FETCH o.member m " +
                                "WHERE o.id = :id",
                        Order.class
                )
                .setParameter("id", orderId)
                .getResultStream()
                .findFirst();
    }

    private List<OrderItem> findOrderItemsByOrderId(String orderId) {
        return this.em.createQuery(
                "SELECT oi " +
                        "FROM OrderItem oi " +
                        "JOIN FETCH oi.item i " +
                        "WHERE oi.order.id = :orderId",
                OrderItem.class
        )
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
