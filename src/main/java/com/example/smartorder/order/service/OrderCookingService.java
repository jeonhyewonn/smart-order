package com.example.smartorder.order.service;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.exception.InsufficientIngredientException;
import com.example.smartorder.item.exception.NotFoundItemException;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.domain.OrderItem;
import com.example.smartorder.order.exception.NotFoundOrderException;
import com.example.smartorder.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrderCookingService {
    private final OrderRepository orderRepository;

    @Transactional
    public Order cook(CreateOrderMessage orderMessage) {
        Order order = this.orderRepository.findById(orderMessage.getId()).orElseThrow(NotFoundOrderException::new);
        List<OrderItem> orderItems = order.getOrderItems();
        Map<Long, Item> itemMap = getItemMap(orderItems);

        if (!hasAllItems(orderMessage, itemMap)) throw new NotFoundItemException();

        for (OrderItem orderItem : orderItems) {
            Item item = itemMap.get(orderItem.getItem().getId());
            int quantity = orderItem.getQuantity();

            if (!item.hasSufficientIngredients(quantity)) throw new InsufficientIngredientException();

            item.deductIngredients(quantity);
        }

        order.toCookedState();

        return order;
    }

    private boolean hasAllItems(CreateOrderMessage orderMessage, Map<Long, Item> itemMap) {
        boolean hasAllItems = orderMessage.getOrderItems().stream()
                .allMatch(message -> itemMap.containsKey(message.getItemId()));
        return hasAllItems;
    }

    private Map<Long, Item> getItemMap(List<OrderItem> orderItems) {
        Map<Long, Item> itemMap = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            itemMap.put(item.getId(), item);
        }

        return itemMap;
    }
}
