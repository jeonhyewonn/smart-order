package com.example.smartorder.item;

import com.example.smartorder.item.domain.Item;

import java.time.LocalDateTime;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class ItemMock {
    private static class TestItem extends Item {
        public TestItem(Long id, String name, Double price) {
            super();
            setField(this, "id", id);
            setField(this, "name", name);
            setField(this, "price", price);
            setField(this, "isDeleted", false);
            setField(this, "createdAt", LocalDateTime.now());
        }
    }
    public static final Item item = new TestItem(1L, "유자차", 5000.0);
}
