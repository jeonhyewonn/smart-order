package com.example.smartorder.item.service;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.exception.InsufficientIngredientException;
import com.example.smartorder.item.exception.NotFoundItemException;
import com.example.smartorder.item.repository.ItemRepository;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderItemMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class ItemCookService {
    private final ItemRepository itemRepository;

    @Transactional
    public void cook(List<CreateOrderItemMessage> createOrderItems) {
        Map<Long, Item> itemMap = getItemMap(createOrderItems);

        if (!hasAllItems(createOrderItems, itemMap)) throw new NotFoundItemException();

        for (CreateOrderItemMessage createOrderItem : createOrderItems) {
            Item item = itemMap.get(createOrderItem.getItemId());
            int quantity = createOrderItem.getQuantity();

            if (!item.hasSufficientIngredients(quantity)) throw new InsufficientIngredientException();

            item.deductIngredients(quantity);
        }
    }

    private boolean hasAllItems(List<CreateOrderItemMessage> createOrderItems, Map<Long, Item> itemMap) {
        return createOrderItems.stream()
                .allMatch(orderItem -> itemMap.containsKey(orderItem.getItemId()));
    }

    private Map<Long, Item> getItemMap(List<CreateOrderItemMessage> createOrderItems) {
        List<Item> items = this.itemRepository.findByIds(getItemIds(createOrderItems));

        Map<Long, Item> itemMap = new HashMap<>();
        for (Item item : items) {
            itemMap.put(item.getId(), item);
        }

        return itemMap;
    }

    private List<Long> getItemIds(List<CreateOrderItemMessage> createOrderItems) {
        return createOrderItems.stream()
                .map(CreateOrderItemMessage::getItemId)
                .collect(toList());
    }
}
