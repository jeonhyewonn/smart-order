package com.example.smartorder.item.service;

import com.example.smartorder.item.ItemMock;
import com.example.smartorder.item.domain.Ingredient;
import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.domain.ItemIngredient;
import com.example.smartorder.item.exception.InsufficientIngredientException;
import com.example.smartorder.item.exception.NotFoundItemException;
import com.example.smartorder.item.repository.ItemRepository;
import com.example.smartorder.order.OrderMock;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderItemMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class ItemCookServiceTest {
    private final ItemRepository itemRepository;
    private final ItemCookService itemCookService;
    private Item item;
    private List<CreateOrderItemMessage> orderItemMessages;

    public ItemCookServiceTest() {
        this.itemRepository = Mockito.mock(ItemRepository.class);
        this.itemCookService = new ItemCookService(
                this.itemRepository
        );
    }

    @BeforeEach
    public void beforeEach() {
        this.item = new ItemMock().item;
        this.orderItemMessages = new OrderMock().createOrderMessage.getOrderItems();
    }

    private List<Long> getItemIds() {
        return orderItemMessages.stream()
                .map(CreateOrderItemMessage::getItemId)
                .collect(toList());
    }

    @Test
    public void cookWithNotFoundItemThrowNotFoundItemException() {
        // Given
        when(this.itemRepository.findByIds(getItemIds()))
                .thenReturn(List.of());

        // Then
        assertThatThrownBy(() -> {
            this.itemCookService.cook(this.orderItemMessages);
        }).isInstanceOf(NotFoundItemException.class);
    }

    

    @Test
    public void cookWithInsufficientIngredientsThrowInsufficientIngredientException() {
        // Given
        when(this.itemRepository.findByIds(getItemIds()))
                .thenReturn(List.of(this.item));

        int maxIngredientStock = this.item.getItemIngredients().stream()
                .map((itemIngredient) -> itemIngredient.getIngredient().getStock())
                .max(Integer::compare)
                .orElse(0);
        for (CreateOrderItemMessage orderItem : this.orderItemMessages) {
            setField(orderItem, "quantity", maxIngredientStock + 1);
        }

        // Then
        assertThatThrownBy(() -> {
            this.itemCookService.cook(this.orderItemMessages);
        }).isInstanceOf(InsufficientIngredientException.class);
    }

    @Test
    public void cookWillSucceed() {
        // Given
        when(this.itemRepository.findByIds(getItemIds()))
                .thenReturn(List.of(this.item));

        int minIngredientStock = this.item.getItemIngredients().stream()
                .map((itemIngredient) -> itemIngredient.getIngredient().getStock())
                .min(Integer::compare)
                .orElse(0);
        for (CreateOrderItemMessage orderItem : this.orderItemMessages) {
            setField(orderItem, "quantity", minIngredientStock);
        }
        
        // compare
        Map<Long, Integer> beforeStockMap = getStockMap(this.item.getItemIngredients());

        // When
        this.itemCookService.cook(this.orderItemMessages);

        // Then
        List<Item> madeItems = this.itemRepository.findByIds(getItemIds());
        Map<Long, Item> madeItemMap = new HashMap<>();
        for (Item item : madeItems) {
            madeItemMap.put(item.getId(), item);
        }

        for (CreateOrderItemMessage orderItem : this.orderItemMessages) {
            Item madeItem = madeItemMap.get(orderItem.getItemId());
            Map<Long, Integer> afterStockMap = getStockMap(madeItem.getItemIngredients());
            afterStockMap.forEach((key, value) -> {
                assertThat(beforeStockMap.get(key) - orderItem.getQuantity()).isEqualTo(value);
            });
        }
    }

    private Map<Long, Integer> getStockMap(List<ItemIngredient> itemIngredients) {
        Map<Long, Integer> stockMap = new HashMap<>();
        for (ItemIngredient itemIngredient : itemIngredients) {
            Ingredient ingredient = itemIngredient.getIngredient();
            stockMap.put(ingredient.getId(), ingredient.getStock());
        }

        return stockMap;
    }
}